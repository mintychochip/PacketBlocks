package org.aincraft.loot;

import java.util.Collection;
import java.util.List;
import org.aincraft.loot.LootData.CustomLootData;
import org.bukkit.inventory.ItemStack;

public class CustomLootDataImpl implements CustomLootData {

  @Override
  public Collection<ItemStack> loot(LootContext context) {
    return List.of();
  }
}
