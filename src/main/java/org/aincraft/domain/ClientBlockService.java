package org.aincraft.domain;

import org.aincraft.api.BlockBinding;
import org.aincraft.api.ClientBlock;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClientBlockService {

  @NotNull
  ClientBlock upsertBlock(BlockBinding binding);

  @Nullable
  ClientBlock loadBlock(Location location);

  boolean deleteBlock(@NotNull Location location);
}
