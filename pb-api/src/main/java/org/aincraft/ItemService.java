package org.aincraft;

import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

public interface ItemService {

  @NotNull
  static ItemService itemService() {
    return Bridge.bridge().itemService();
  }

  Optional<String> read(ItemStack itemStack);

  void write(ItemStack itemStack, String resourceKey);
}
