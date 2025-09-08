package org.aincraft;

import org.aincraft.api.ClientBlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public interface PacketItemService {

  boolean isPacketItem(ItemStack stack);

  @NotNull
  ClientBlockData readPacketData(ItemStack stack) throws IllegalArgumentException;

  void writePacketData(ItemStack stack, ClientBlockData data);
}
