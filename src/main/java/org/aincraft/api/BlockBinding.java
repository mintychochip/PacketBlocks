package org.aincraft.api;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public interface BlockBinding {

  static BlockBinding create(String worldName, double x, double y, double z, String resourceKey)
      throws IllegalArgumentException {
    return BlockBindingImpl.create(worldName, x, y, z, resourceKey);
  }

  static BlockBinding create(Location location, String resourceKey) {
    return new BlockBindingImpl(location, resourceKey);
  }

  String worldName();

  double x();

  double y();

  double z();

  Chunk chunk();

  int chunkX();

  int chunkZ();

  Location location();

  String resourceKey();
}
