package org.aincraft.api;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface ClientBlock {

  ClientBlockData getBlockData();

  Vector getPosition();

  void setBlockData(ClientBlockData blockData);

  boolean visibleTo(Player player);

  void show(Player player);

  void hide(Player player);

  void teleport(Vector position);

  Set<Player> viewers();

  interface Builder {

    Builder setBlockData(ClientBlockData blockData);

    Builder setLocation(Location location);

    ClientBlock build();
  }

  interface Factory {

    ClientBlock create(BlockData blockData);
  }
}
