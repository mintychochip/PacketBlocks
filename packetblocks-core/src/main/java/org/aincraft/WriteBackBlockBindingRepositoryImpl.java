package org.aincraft;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.ArrayList;
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
import javax.naming.Binding;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WriteBackBlockBindingRepositoryImpl implements BlockBindingRepository {

  private static final Duration TTL = Duration.ofMinutes(10);
  @NotNull
  private final BlockBindingRepository delegate;
  private final Cache<LocationKey, BlockBinding> readCache = Caffeine.newBuilder()
      .expireAfterAccess(TTL).build();
  private final Map<LocationKey, BlockBinding> pendingUpdates = new ConcurrentHashMap<>();
  private final Set<LocationKey> pendingDeletes = ConcurrentHashMap.newKeySet();
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
    LocationKey locationKey = LocationKey.create(location);
    if (pendingDeletes.contains(locationKey)) {
      return null;
    }
    BlockBinding binding = pendingUpdates.get(locationKey);
    if (binding != null) {
      return binding;
    }
    return readCache.get(locationKey, key -> delegate.load(location));
  }

  @Override
  public boolean delete(Location location) {
    LocationKey locationKey = LocationKey.create(location);
    pendingDeletes.add(locationKey);
    pendingUpdates.remove(locationKey);
    readCache.invalidate(locationKey);
    return true;
  }

  @Override
  public boolean save(BlockBinding binding) {
    LocationKey locationKey = LocationKey.create(binding.location());
    pendingDeletes.remove(locationKey);
    pendingUpdates.put(locationKey, binding);
    readCache.put(locationKey, binding);
    return true;
  }

  @Override
  public List<BlockBinding> loadAllByChunk(Chunk chunk) {
    List<BlockBinding> stale = delegate.loadAllByChunk(chunk);
    Map<LocationKey, BlockBinding> merged = new HashMap<>();
    for (BlockBinding binding : stale) {
      merged.put(LocationKey.create(binding.location()), binding);
    }
    for (LocationKey key : pendingDeletes) {
      int cx = chunk.getX();
      int cz = chunk.getZ();
      if (key.cx() == cx && key.cz() == cz) {
        merged.remove(key);
      }
    }
    for (LocationKey key : pendingUpdates.keySet()) {
      BlockBinding binding = pendingUpdates.get(key);
      if (binding == null) {
        continue;
      }
      merged.put(key,binding);
    }
    return new ArrayList<>(merged.values());
  }

  private void flush() {
    if (!flush.compareAndSet(false, true)) {
      return;
    }
    Map<LocationKey, BlockBinding> batchUpdates = new HashMap<>();
    Iterator<Entry<LocationKey, BlockBinding>> updateIterator = pendingUpdates.entrySet()
        .iterator();
    while (updateIterator.hasNext() && batchUpdates.size() < batchSize) {
      Entry<LocationKey, BlockBinding> entry = updateIterator.next();
      LocationKey key = entry.getKey();
      BlockBinding value = entry.getValue();
      if (pendingUpdates.remove(key, value)) {
        batchUpdates.put(key, value);
      }
    }
    Set<LocationKey> batchDeletes = new HashSet<>();
    Iterator<LocationKey> deleteIterator = pendingDeletes.iterator();
    while (deleteIterator.hasNext() && batchDeletes.size() < batchSize) {
      LocationKey key = deleteIterator.next();
      if (pendingDeletes.remove(key)) {
        batchDeletes.add(key);
      }
    }
    try {
      batchUpdates.forEach((__, value) -> {
        delegate.save(value);
      });
      batchDeletes.forEach(key -> delegate.delete(key.location()));
    } catch (Throwable t) {
      pendingUpdates.putAll(batchUpdates);
      pendingDeletes.addAll(batchDeletes);
    } finally {
      flush.set(false);
    }
  }
}
