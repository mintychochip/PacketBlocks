package org.aincraft;

import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

@AvailableSince("1.0.2")
public interface ItemService {

  @NotNull
  @AvailableSince("1.0.2")
  static ItemService itemService() {
    return Bridge.bridge().itemService();
  }

  Optional<String> read(ItemStack itemStack);

  void write(ItemStack itemStack, String resourceKey);
}
