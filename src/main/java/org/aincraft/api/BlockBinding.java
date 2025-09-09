package org.aincraft.api;

import java.sql.Timestamp;
import java.util.Objects;
import org.aincraft.api.ModelData.Record;
import org.aincraft.domain.Repository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public interface BlockBinding {

  ModelData blockData();

  Location location();

  record Record(String world, double x, double y, double z, int cx, int cz,
                ModelData.Record blockData) implements Repository.Record<Location> {

    @Override
    public Location key() {
      World w = Bukkit.getWorld(world);
      return new Location(w, x, y, z);
    }
  }
}
