package org.aincraft.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public sealed interface Registry<T extends Keyed> permits SimpleRegistryImpl {

  static <T extends Keyed> Registry<T> createSimple() {
    return new SimpleRegistryImpl<>();
  }

  void register(T object);

  @NotNull
  T get(Key key) throws IllegalArgumentException;

  default boolean isRegistered(T object) {
    return isRegistered(object.key());
  }

  boolean isRegistered(Key key);
}
