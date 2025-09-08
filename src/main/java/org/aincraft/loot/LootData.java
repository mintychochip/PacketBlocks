package org.aincraft.loot;

import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

public interface LootData {

  Collection<ItemStack> loot(LootContext context);

  interface CustomLootData extends LootData {

  }
}
