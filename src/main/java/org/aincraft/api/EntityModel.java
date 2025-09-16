package org.aincraft.api;

import java.util.Set;
import org.aincraft.api.EntityModelImpl.EntityModelMetaImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityModel {

  static EntityModel create() {
    return new EntityModelImpl<>(EntityModelMetaImpl.create(),);
  }

  boolean isVisible(Player player);

  void showTo(Player player);

  void hideFrom(Player player);

  void teleport(Location location);

  Set<Player> getViewers();

  void setMeta(EntityModelMeta entityModelMeta);

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
