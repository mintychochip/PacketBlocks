package org.aincraft;

import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ClientBlock {

  Key getModel();

  boolean visibleTo(Player player);

  void show(Player player);

  void move(Location location);

  Location getBlockLocation();

  net.minecraft.world.entity.Display.ItemDisplay getDisplay();

  interface Builder {

    Builder setItemModel(Key itemModel);

    Builder setLocation(Location location);

    Builder setTransformation(Transformation transformation);

    Builder addViewer(Entity viewer);

    ClientBlock build();
  }
}
