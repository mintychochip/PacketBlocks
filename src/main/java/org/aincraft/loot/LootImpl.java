package org.aincraft.loot;

import org.bukkit.entity.Player;

abstract sealed class LootImpl implements Loot permits ExperienceLootImpl, ItemLootImpl {

  protected final int min, max;

  public LootImpl(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public int min() {
    return min;
  }

  @Override
  public int max() {
    return max;
  }
}
