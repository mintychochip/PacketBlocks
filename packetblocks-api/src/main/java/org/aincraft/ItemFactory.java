package org.aincraft;

import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface ItemFactory {

  default ItemStack create(Key resourceKey) {
    return create(resourceKey,  1);
  }

  ItemStack create(Key resourceKey, int amount) throws IllegalArgumentException;
}
