package org.aincraft.domain;

import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.ClientBlock;
import org.bukkit.Location;

public class ClientBlockManager {
  private final Map<Location, ClientBlock> blockMap = new HashMap<>();

  public boolean isOccupied(Location location) {
    if (blockMap.containsKey(location)) {
      return true;
    }
    return false;
  }


}
