package org.aincraft;

import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public interface BlockModelService {

  boolean save(@NotNull BlockBinding blockBinding);

  boolean isModelLoaded(@NotNull Location location);

  @NotNull
  BlockModel loadModel(@NotNull Location location) throws IllegalArgumentException;

  boolean delete(Location location);
}
