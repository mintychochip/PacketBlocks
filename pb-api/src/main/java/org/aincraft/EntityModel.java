package org.aincraft;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public interface EntityModel {

  static EntityModel create(EntityType entityType, Location location) {
    return EntityModelImpl.create(entityType, location.getWorld(),location.toVector());
  }

  boolean isVisible(Player player);

  void showTo(Player player);

  void hideFrom(Player player);

  void teleport(Location location);

  Set<Player> getViewers();

  void setMeta(EntityModelData entityModelData);

  EntityModelData getMeta();

}
