package org.aincraft.registry;

import net.kyori.adventure.key.Keyed;

public interface RegistryContainer {

  <V extends Keyed> Registry<V> get(RegistryKey<V> key);

  interface RegistryKey<V> {

  }
}
