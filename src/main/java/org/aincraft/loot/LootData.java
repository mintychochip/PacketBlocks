package org.aincraft.loot;

import java.util.Collection;
import org.bukkit.inventory.ItemStack;

public interface LootData {

  Collection<ItemStack> loot(LootContext context);

  interface CustomLootData extends LootData {
    record Rule() {}
  }

  record Record
}
