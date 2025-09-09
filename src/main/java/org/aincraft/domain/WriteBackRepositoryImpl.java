package org.aincraft.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class WriteBackRepositoryImpl<K, V extends Repository.Record<K>> implements
    Repository.Writable<K, V> {

  protected final Repository.Writable<K, V> delegate;
  protected final Cache<K, V> readCache = Caffeine.newBuilder()
      .expireAfterAccess(Duration.ofMinutes(10)).build();
  protected final Map<K, V> pendingUpdates = new ConcurrentHashMap<>();
  protected final Set<K> pendingDeletes = ConcurrentHashMap.newKeySet();
  private final WriteBackConfiguration configuration;
  private final AtomicBoolean flush = new AtomicBoolean(false);
  private final AtomicBoolean running = new AtomicBoolean(false);

  WriteBackRepositoryImpl(Repository.Writable<K, V> delegate,
      WriteBackConfiguration configuration) {
    this.delegate = delegate;
    this.configuration = configuration;
  }

  void run(Plugin plugin) {
    if (running.compareAndSet(false, true)) {
      Bukkit.getAsyncScheduler().runAtFixedRate(plugin, task -> {
        flush();
      }, configuration.initialDelay(), configuration.period(), configuration.unit());
    }
  }

  @Override
  public boolean save(@NotNull V value) {
    K key = value.key();
    readCache.put(key, value);
    pendingDeletes.remove(key);
    pendingUpdates.put(key, value);
    return true;
  }

  @Override
  public boolean delete(@NotNull K key) {
    readCache.invalidate(key);
    pendingDeletes.add(key);
    pendingUpdates.remove(key);
    return true;
  }

  @Override
  public @Nullable V load(K key) {
    if (pendingDeletes.contains(key)) {
      return null;
    }
    V value = pendingUpdates.get(key);
    if (value != null) {
      return value;
    }
    return readCache.get(key, delegate::load);
  }

  private void flush() {
    if (!flush.compareAndSet(false, true)) {
      return;
    }
    Map<K, V> batchUpdates = new HashMap<>();
    Iterator<Entry<K, V>> updateIterator = pendingUpdates.entrySet().iterator();
    while (!updateIterator.hasNext() && batchUpdates.size() < configuration.batchSize()) {
      Entry<K, V> entry = updateIterator.next();
      K key = entry.getKey();
      V value = entry.getValue();
      if (pendingUpdates.remove(key, value)) {
        batchUpdates.put(key, value);
      }
    }
    Set<K> batchDeletes = new HashSet<>();
    Iterator<K> deleteIterator = pendingDeletes.iterator();
    while (!deleteIterator.hasNext() && batchDeletes.size() < configuration.batchSize()) {
      K key = deleteIterator.next();
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

  record WriteBackConfiguration(long initialDelay, long period, int batchSize, TimeUnit unit) {

  }
}
