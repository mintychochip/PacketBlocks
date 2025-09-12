package org.aincraft.domain;

import java.util.List;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Service {

  boolean saveBlockBinding(@NotNull BlockBinding blockBinding);

  boolean isPacketItem(ItemStack itemStack);

  @Nullable
  BlockModel getBlock(Location location);

  List<BlockBinding> getBindings(Chunk chunk);

  String readPacketData(ItemStack itemStack) throws IllegalArgumentException;
}
