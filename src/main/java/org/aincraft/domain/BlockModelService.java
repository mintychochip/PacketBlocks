package org.aincraft.domain;

import org.aincraft.api.BlockBinding;
import org.aincraft.api.EntityModel;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface BlockModelService {

  boolean save(BlockBinding blockBinding);

  @Nullable
  EntityModel load(Location location);

  boolean delete(Location location);
}
