package org.aincraft.api;

import java.util.Set;
import org.aincraft.api.EntityModelImpl.EntityModelMetaImpl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityModel {

  static EntityModel create(EntityType entityType, Location location) {
    return EntityModelImpl.create(entityType, location.getWorld(),location.toVector());
  }

  boolean isVisible(Player player);

  void showTo(Player player);

  void hideFrom(Player player);

  void teleport(Location location);

  Set<Player> getViewers();

  void setMeta(EntityModelMeta entityModelMeta);

  EntityModelMeta getMeta();

  interface EntityModelMeta {

    @NotNull
    <T> T getAttribute(EntityModelAttribute<T> attribute, T def);

    @Nullable
    <T> T getAttribute(EntityModelAttribute<T> attribute);

    <T> void setAttribute(EntityModelAttribute<T> attribute, T value);

  }

  interface EntityModelAttribute<T> {

  }
}
