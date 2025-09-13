package org.aincraft.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.aincraft.api.BlockBinding;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WriteBackBlockBindingRepositoryImpl implements BlockBindingRepository {

  @NotNull
  private final BlockBindingRepository delegate;
  private final Cache<Location, BlockBinding> readCache = Caffeine.newBuilder()
      .expireAfterAccess(Duration.ofMinutes(10)).build();
  private final Map<Location, BlockBinding> pendingUpdates = new ConcurrentHashMap<>();
  private final Set<Location> pendingDeletes = ConcurrentHashMap.newKeySet();
  private final int batchSize;
  private final AtomicBoolean flush = new AtomicBoolean(false);

  private WriteBackBlockBindingRepositoryImpl(@NotNull BlockBindingRepository delegate,
      int batchSize) {
    this.delegate = delegate;
    this.batchSize = batchSize;
  }

  static BlockBindingRepository create(Plugin plugin, int batchSize, Duration period,
      BlockBindingRepository delegate) {
    WriteBackBlockBindingRepositoryImpl repository = new WriteBackBlockBindingRepositoryImpl(
        delegate, batchSize);
    Bukkit.getAsyncScheduler()
        .runAtFixedRate(plugin, task -> {
          repository.flush();
        }, 0L, period.toMillis(), TimeUnit.MILLISECONDS);
    return repository;
  }

  @Override
  public @Nullable BlockBinding load(Location location) {
    if (pendingDeletes.contains(location)) {
      return null;
    }
    BlockBinding binding = pendingUpdates.get(location);
    if (binding != null) {
      return binding;
    }
    return readCache.get(location, delegate::load);
  }

  @Override
  public boolean delete(Location location) {
    pendingDeletes.add(location);
    pendingUpdates.remove(location);
    readCache.invalidate(location);
    return true;
  }

  @Override
  public boolean save(BlockBinding blockBinding) {
    pendingDeletes.remove(blockBinding.location());
    pendingUpdates.put(blockBinding.location(), blockBinding);
    readCache.put(blockBinding.location(), blockBinding);
    return true;
  }

  @Override
  public List<BlockBinding> loadAllByChunk(Chunk chunk) {
    return delegate.loadAllByChunk(chunk);
  }

  private void flush() {
    if (!flush.compareAndSet(false, true)) {
      return;
    }
    Map<Location, BlockBinding> batchUpdates = new HashMap<>();
    Iterator<Entry<Location, BlockBinding>> updateIterator = pendingUpdates.entrySet().iterator();
    while (updateIterator.hasNext() && batchUpdates.size() < batchSize) {
      Entry<Location, BlockBinding> entry = updateIterator.next();
      Location key = entry.getKey();
      BlockBinding value = entry.getValue();
      if (pendingUpdates.remove(key, value)) {
        batchUpdates.put(key, value);
      }
    }
    Set<Location> batchDeletes = new HashSet<>();
    Iterator<Location> deleteIterator = pendingDeletes.iterator();
    while (deleteIterator.hasNext() && batchDeletes.size() < batchSize) {
      Location key = deleteIterator.next();
      if (!pendingDeletes.remove(key)) {
        batchDeletes.add(key);
      }
    }
    try {
      batchUpdates.forEach((__, value) -> {
        delegate.save(value);
      });
      batchDeletes.forEach(delegate::delete);
    } catch (Throwable t) {
      pendingUpdates.putAll(batchUpdates);
      pendingDeletes.addAll(batchDeletes);
    } finally {
      flush.set(false);
    }
  }

}
