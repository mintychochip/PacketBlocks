package org.aincraft.api;

import org.bukkit.Location;

public interface BlockBinding {

  ClientBlockData blockData();

  Location location();

  record Record(String world, double x, double y, double z, int cx, int cz,
                ClientBlockData.Record blockData) {

  }
}
