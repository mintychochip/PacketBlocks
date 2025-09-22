package org.aincraft;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface BlockModelService {

  boolean save(BlockBinding blockBinding);

  @Nullable
  EntityModel load(Location location);

  boolean delete(Location location);
}
