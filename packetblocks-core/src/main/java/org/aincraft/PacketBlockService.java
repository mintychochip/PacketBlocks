package org.aincraft;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface PacketBlockService {

  boolean delete(Location location);

  PacketBlock save(BlockBinding blockBinding);

  @Nullable
  PacketBlock load(Location location);

  List<PacketBlock> loadAll(Chunk chunk);
}