package org.aincraft.domain;

import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClientBlockService {

  @NotNull
  BlockModel upsertBlock(BlockBinding binding);

  @Nullable
  BlockModel loadBlock(Location location);

  boolean deleteBlock(@NotNull Location location);
}
