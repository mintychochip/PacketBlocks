package org.aincraft;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface KeyedItem extends Keyed {

  static KeyedItem create(ItemStack itemStack, Key key) {
    return new KeyedItem() {
      @Override
      public ItemStack getItemStack() {
        return itemStack;
      }

      @Override
      public @NotNull Key key() {
        return key;
      }
    };
  }

  ItemStack getItemStack();
}
