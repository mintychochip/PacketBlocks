package org.aincraft.api;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface BlockModel {

  ModelData getBlockData();

  Vector getPosition();

  void setBlockData(ModelData blockData);

  boolean visibleTo(Player player);

  void show(Player player);

  void hide(Player player);

  void teleport(Vector position);

  Set<Player> viewers();

  interface Builder {

    Builder setBlockData(ModelData blockData);

    Builder setLocation(Location location);

    BlockModel build();
  }

  interface Factory {

    BlockModel create(BlockData blockData);
  }
}
