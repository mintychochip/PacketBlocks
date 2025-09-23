package org.aincraft;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
record LocationKey(String world, double x, double y, double z, int cx, int cz) {

  static LocationKey create(Location location) {
    World world = location.getWorld();
    Chunk chunk = location.getChunk();
    return new LocationKey(world.getName(), location.x(), location.y(), location.z(), chunk.getX(),
        chunk.getZ());
  }

  Location location() {
    World world = Bukkit.getWorld(this.world);
    return new Location(world, x, y, z);
  }
}
