package org.aincraft;

import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

@Internal
public interface BlockBindingRepository {

  @Nullable
  BlockBinding load(Location location);

  boolean delete(Location location);

  boolean save(BlockBinding binding);

  List<BlockBinding> loadAllByChunk(Chunk chunk);
}
