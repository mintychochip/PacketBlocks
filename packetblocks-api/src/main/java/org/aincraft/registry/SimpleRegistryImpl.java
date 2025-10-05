package org.aincraft.registry;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

final class SimpleRegistryImpl<T extends Keyed> implements Registry<T> {

  private final Map<Key, T> registrar = new HashMap<>();

  @Override
  public void register(T object) {
    registrar.put(object.key(), object);
  }

  @Override
  public @NotNull T get(Key key) throws IllegalArgumentException {
    Preconditions.checkArgument(isRegistered(key));
    return registrar.get(key);
  }

  @Override
  public boolean isRegistered(Key key) {
    return registrar.containsKey(key);
  }

  @Override
  public Set<Key> keySet() {
    return registrar.keySet();
  }
}
