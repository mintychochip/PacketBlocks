package org.aincraft;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

@AvailableSince("1.0.2")
public interface EntityModel {

  static EntityModel create(EntityType entityType, Location location) {
    return Bridge.bridge().packetBlockFactory().create(entityType, location);
  }

  boolean isVisible(Player player);

  void showTo(Player player);

  void hideFrom(Player player);

  @AvailableSince("1.0.2")
  void move(Location location);

  void teleport(Location location);

  Set<Player> getViewers();

  void setData(EntityModelData data);

  EntityModelData getData();

}
