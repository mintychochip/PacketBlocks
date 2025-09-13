package org.aincraft.domain;

import org.aincraft.domain.Repository.Record;
import org.jetbrains.annotations.Nullable;

public interface Repository<K, V extends Record<K>> {

  @Nullable
  V load(K key);

  interface Writable<K, V extends Record<K>> extends Repository<K, V> {

    boolean save(V value);

    boolean delete(K key);
  }

  interface Record<K> {

    K key();
  }
}
