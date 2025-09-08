package org.aincraft;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.aincraft.api.LootData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

public class VanillaBackedLootDataImpl implements LootData {

  private final LootTable lootTable;
  private final ThreadLocalRandom localRandom = ThreadLocalRandom.current();

  public VanillaBackedLootDataImpl(LootTable lootTable) {
    this.lootTable = lootTable;
  }

  @Override
  public Collection<ItemStack> loot(LootContext context) {
    return lootTable.populateLoot(localRandom,context);
  }
}
