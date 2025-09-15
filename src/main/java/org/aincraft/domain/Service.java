package org.aincraft.domain;

import org.bukkit.inventory.ItemStack;

public interface Service {

  boolean isPacketItem(ItemStack itemStack);

  String readPacketData(ItemStack itemStack) throws IllegalArgumentException;
}
