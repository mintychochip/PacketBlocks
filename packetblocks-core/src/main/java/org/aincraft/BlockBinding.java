package org.aincraft;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.util.InternalApi;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public sealed interface BlockBinding permits BlockBindingImpl {

  @InternalApi
  static BlockBinding create(String worldName, double x, double y, double z, String resourceKey)
      throws IllegalArgumentException {
    World world = Bukkit.getWorld(worldName);
    Preconditions.checkArgument(world != null, "Unknown world: " + worldName);
    return new BlockBindingImpl(new Location(world, x, y, z), resourceKey);
  }

  Location location();

  String resourceKey();

  default String worldName() {
    return location().getWorld().getName();
  }

  default double x() {
    return location().getX();
  }

  default double y() {
    return location().getY();
  }

  default double z() {
    return location().getZ();
  }

  default Chunk chunk() {
    return location().getChunk();
  }

  default int chunkX() {
    return chunk().getX();
  }

  default int chunkZ() {
    return chunk().getZ();
  }

}
