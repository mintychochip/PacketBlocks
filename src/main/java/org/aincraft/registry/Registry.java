package org.aincraft.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.aincraft.loot.LootData.LootFunction;
import org.jetbrains.annotations.Nullable;

public interface Registry<V extends Keyed> {

  Registry<LootFunction> LOOT_FUNCTIONS = new RegistryImpl<>();

  @Nullable
  V get(Key key);

  V getOrThrow(Key key) throws IllegalArgumentException;

  boolean isRegistered(Key key);

  void register(V object);
}
