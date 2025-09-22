package org.aincraft;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import org.aincraft.BlockBinding.BlockBindingImpl;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public sealed interface BlockBinding permits BlockBindingImpl {

  static BlockBinding create(String worldName, double x, double y, double z, String resourceKey)
      throws IllegalArgumentException {
    World world = Bukkit.getWorld(worldName);
    Preconditions.checkArgument(world != null, "Unknown world: " + worldName);
    return new BlockBindingImpl(new Location(world, x, y, z), resourceKey);
  }

  static BlockBinding create(Location location, String resourceKey) {
    return new BlockBindingImpl(location, resourceKey);
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

  record BlockBindingImpl(Location location, String resourceKey) implements BlockBinding {

  }
}
