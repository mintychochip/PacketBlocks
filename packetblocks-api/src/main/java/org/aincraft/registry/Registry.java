package org.aincraft.registry;

import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

@AvailableSince("1.0.2")
public sealed interface Registry<T extends Keyed> permits SimpleRegistryImpl {

  @NotNull
  @AvailableSince("1.0.2")
  static <T extends Keyed> Registry<T> simple() {
    return new SimpleRegistryImpl<>();
  }

  void register(T object);

  @NotNull
  T get(Key key) throws IllegalArgumentException;

  default boolean isRegistered(T object) {
    return isRegistered(object.key());
  }

  boolean isRegistered(Key key);

  Set<Key> keySet();
}
