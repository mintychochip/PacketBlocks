package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.domain.Repository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public interface BlockBinding {

  Key resourceKey();

  ModelData blockData();

  Location location();

  record Record(String world, double x, double y, double z, int cx, int cz, String resourceKey,
                ModelDataRecord blockData) implements Repository.Record<Location> {

    @Override
    public Location key() {
      World w = Bukkit.getWorld(world);
      return new Location(w, x, y, z);
    }
  }
}
