package org.aincraft.loot;

import java.util.List;
import org.aincraft.loot.Loot.LootInstance;
import org.aincraft.loot.triggers.TriggerOnDrop;
import org.bukkit.inventory.ItemStack;

//TODO: reduce visibility
public record ItemLootInstanceImpl(int amount, ItemStack itemStack) implements LootInstance,
    TriggerOnDrop {

  @Override
  public void onDrop(DropItemContext context) {
    List<ItemStack> items = context.items();
    items.add(itemStack);
    context.items(items);
  }
}
