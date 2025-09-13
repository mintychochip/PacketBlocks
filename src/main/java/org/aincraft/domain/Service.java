package org.aincraft.domain;

import java.util.List;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockBindingImpl;
import org.aincraft.api.BlockModel;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Service {

  boolean isPacketItem(ItemStack itemStack);

  String readPacketData(ItemStack itemStack) throws IllegalArgumentException;
}
