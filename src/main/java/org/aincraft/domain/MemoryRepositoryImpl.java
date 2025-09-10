package org.aincraft.domain;

import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class MemoryRepositoryImpl<K, V extends Repository.Record<K>> implements Repository<K, V> {

  private final Map<K, V> map;

  public MemoryRepositoryImpl(Map<K, V> map) {
    this.map = map;
  }

  @Override
  public @Nullable V load(K key) {
    return map.get(key);
  }
}
