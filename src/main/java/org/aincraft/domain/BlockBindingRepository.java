package org.aincraft.domain;

import java.util.List;
import org.aincraft.api.BlockBinding;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockBindingRepository {

  boolean save(@NotNull BlockBinding.Record record);

  boolean delete(@NotNull Location location);

  @Nullable
  BlockBinding.Record load(Location location);

  List<BlockBinding.Record> loadAllByChunk(Chunk chunk);
}
