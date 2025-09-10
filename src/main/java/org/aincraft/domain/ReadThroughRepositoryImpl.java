package org.aincraft.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.jetbrains.annotations.Nullable;

final class ReadThroughRepositoryImpl<K, V extends Repository.Record<K>> implements
    Repository<K, V> {

  private final Cache<K, V> readCache = Caffeine.newBuilder()
      .expireAfterAccess(Duration.ofMinutes(10)).build();

  private final Repository<K, V> delegate;

  ReadThroughRepositoryImpl(Repository<K, V> delegate) {
    this.delegate = delegate;
  }

  public static <K, V extends Repository.Record<K>> Repository<K, V> decorate(
      Repository<K, V> delegate) {
    return new ReadThroughRepositoryImpl<>(delegate);
  }

  @Override
  public @Nullable V load(K key) {
    V load = readCache.getIfPresent(key);
    if (load != null) {
      return load;
    }
    V delegateLoad = delegate.load(key);
    if (delegateLoad == null) {
      return null;
    }
    readCache.put(key, delegateLoad);
    return delegateLoad;
  }
}
