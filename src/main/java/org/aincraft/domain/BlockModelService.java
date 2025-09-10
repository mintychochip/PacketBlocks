package org.aincraft.domain;

import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface BlockModelService {

  boolean save(BlockBinding binding);

  @Nullable
  BlockModel load(Location location);

  boolean delete(Location location);
}
