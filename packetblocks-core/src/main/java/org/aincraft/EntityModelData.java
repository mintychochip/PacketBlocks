package org.aincraft;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityModelData {

  @NotNull
  <T> T getAttribute(EntityModelAttribute<T> attribute, T def);

  @Nullable
  <T> T getAttribute(EntityModelAttribute<T> attribute);

  <T> void setAttribute(EntityModelAttribute<T> attribute, T value);

  interface EntityModelAttribute<T> {

  }
}
