package org.aincraft.api;

import org.bukkit.Location;

public interface BlockBinding {

  ModelData blockData();

  Location location();

  record Record(String world, double x, double y, double z, int cx, int cz,
                ModelData.Record blockData) {

  }
}
