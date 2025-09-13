package org.aincraft.api;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

record BlockBindingImpl(Location location,
                               String resourceKey) implements BlockBinding {

  static BlockBinding create(String worldName, double x, double y, double z, String resourceKey)
      throws IllegalArgumentException {
    World world = Bukkit.getWorld(worldName);
    Preconditions.checkArgument(world != null);
    Location location = new Location(world, x, y, z);
    return new BlockBindingImpl(location, resourceKey);
  }

  @Override
  public String worldName() {
    return location.getWorld().getName();
  }

  @Override
  public double x() {
    return location.x();
  }

  @Override
  public double y() {
    return location.y();
  }

  @Override
  public double z() {
    return location.z();
  }

  @Override
  public Chunk chunk() {
    return location().getChunk();
  }

  @Override
  public int chunkX() {
    return chunk().getX();
  }

  @Override
  public int chunkZ() {
    return chunk().getZ();
  }
}
