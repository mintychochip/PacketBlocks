package org.aincraft.domain;

import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.BlockModel;
import org.bukkit.Location;

public class ClientBlockManager {
  private final Map<Location, BlockModel> blockMap = new HashMap<>();

  public boolean isOccupied(Location location) {
    if (blockMap.containsKey(location)) {
      return true;
    }
    return false;
  }


}
