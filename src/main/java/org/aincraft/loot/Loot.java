package org.aincraft.loot;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;

public sealed interface Loot permits LootImpl, ItemLootImpl, ExperienceLootImpl {

  static Loot item(int min, int max, Key itemKey) {
    return new ItemLootImpl(min, max, itemKey);
  }

  static Loot experience(int min, int max) {
    return new ExperienceLootImpl(min, max);
  }

  int min();

  int max();

  LootInstance instance(int amount) throws IllegalStateException;

  interface LootInstance {

    int amount();

    void add(Player player);
  }

}
