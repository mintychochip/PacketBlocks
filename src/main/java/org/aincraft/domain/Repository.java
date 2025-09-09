package org.aincraft.domain;

import org.aincraft.domain.Repository.Record;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

public interface Repository<K, V extends Record<K>> {

  @Nullable
  V load(K key);

  interface Writable<K, V extends Record<K>> extends Repository<K, V> {

    boolean save(V value);

    boolean delete(K key);
  }

  interface WriteBack<K, V extends Record<K>> extends Repository.Writable<K, V> {

    void run(Plugin plugin);
  }

  interface Record<K> {

    K key();
  }
}
