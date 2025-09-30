package org.aincraft;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

public interface EntityModel {

  boolean visible(Player player);

  void show(Player player);

  void hide(Player player);

  @AvailableSince("1.0.2")
  void move(Location location);

  void teleport(Location location);

  Set<Player> viewers();

  void setData(EntityModelData data);

  EntityModelData getData();

}
