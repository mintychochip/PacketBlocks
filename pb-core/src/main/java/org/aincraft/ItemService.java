package org.aincraft;

import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface ItemService {

  Optional<String> read(ItemStack itemStack);

  void write(ItemStack itemStack, String resourceKey);
}
