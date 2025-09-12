package org.aincraft.domain;

import org.aincraft.api.BlockBinding;
import org.aincraft.api.PacketBlock;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface PacketBlockService {

  boolean delete(Location location);

  PacketBlock save(BlockBinding blockBinding);

  @Nullable
  PacketBlock load(Location location);
}
