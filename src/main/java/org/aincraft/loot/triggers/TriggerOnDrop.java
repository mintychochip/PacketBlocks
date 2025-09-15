package org.aincraft.loot.triggers;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public interface TriggerOnDrop {

  void onDrop(DropItemContext context);

  interface DropItemContext {

    void items(List<ItemStack> items);

    List<ItemStack> items();
  }
}
