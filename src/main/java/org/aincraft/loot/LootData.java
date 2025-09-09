package org.aincraft.loot;

import java.util.Collection;
import java.util.List;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;

public interface LootData {

  Collection<ItemStack> loot(LootContext context);

  interface LootFunction {

    List<ItemStack> apply(List<ItemStack> items, LootContext context);
  }

  interface LootCondition {
    Registry
  }
}
