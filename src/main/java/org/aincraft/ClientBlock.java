package org.aincraft;

import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

public interface ClientBlock {

  Key getModel();

  boolean visibleTo(Player player);

  void show(Player player);

  void hide(Player player);

  void teleport(Vector position);

  interface Builder {

    Builder setItemModel(Key itemModel);

    Builder setLocation(Location location, boolean aligned);

    Builder setTransformation(Transformation transformation);

    Builder addViewer(Player player);

    Builder setViewRange(float range);

    ClientBlock build();
  }
}
