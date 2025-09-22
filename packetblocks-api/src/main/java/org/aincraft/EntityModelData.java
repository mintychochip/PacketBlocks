package org.aincraft;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AvailableSince("1.0.2")
public interface EntityModelData {

  @NotNull
  static EntityModelData create() {
    return Bridge.bridge().packetBlockFactory().create();
  }

  @NotNull
  <T> T getAttribute(EntityModelAttribute<T> attribute, T def);

  @Nullable
  <T> T getAttribute(EntityModelAttribute<T> attribute);

  <T> void setAttribute(EntityModelAttribute<T> attribute, T value);

  interface EntityModelAttribute<T> {

  }

  interface Factory {

    EntityModelData create();
  }
}
