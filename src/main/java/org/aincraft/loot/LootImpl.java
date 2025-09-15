package org.aincraft.loot;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;

abstract sealed class LootImpl implements Loot permits ExperienceLootImpl, LootImpl.ItemLootImpl {

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

  static final class ItemLootImpl extends LootImpl implements Loot {

    private final Key itemKey;

    ItemLootImpl(int min, int max, Key itemKey) {
      super(min, max);
      this.itemKey = itemKey;
    }

    @Override
    public LootInstance instance(int amount) throws IllegalStateException {
      String namespace = itemKey.namespace();
      if ("minecraft".equals(namespace)) {
        Material material = Registry.MATERIAL.getOrThrow(itemKey);
        return new ItemLootInstanceImpl(amount, ItemStack.of(material, amount));
      }
      return null;
    }
  }
}
