package org.aincraft.registry;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Nullable;

public class RegistryImpl<V extends Keyed> implements Registry<V> {

  private final Map<Key, V> registrar = new HashMap<>();

  @Override
  public @Nullable V get(Key key) {
    return registrar.get(key);
  }

  @Override
  public V getOrThrow(Key key) throws IllegalArgumentException {
    Preconditions.checkArgument(isRegistered(key));
    return get(key);
  }

  @Override
  public boolean isRegistered(Key key) {
    return registrar.containsKey(key);
  }

  @Override
  public void register(V object) {
    registrar.put(object.key(), object);
  }
}
