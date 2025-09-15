package org.aincraft.loot;

import net.kyori.adventure.key.Key;

public sealed interface Loot permits LootImpl, LootImpl.ItemLootImpl, ExperienceLootImpl {

  static Loot item(int min, int max, Key itemKey) {
    return new LootImpl.ItemLootImpl(min, max, itemKey);
  }

  static Loot experience(int min, int max) {
    return new ExperienceLootImpl(min, max);
  }

  int min();

  int max();

  LootInstance instance(int amount) throws IllegalStateException;

  interface LootInstance {

    int amount();

  }
}
