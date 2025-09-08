package org.aincraft.domain;

import java.util.List;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockBinding.Record;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WriteBackBlockBindingRepositoryImpl implements BlockBindingRepository {

  @Override
  public boolean save(@NotNull BlockBinding.Record record) {
    return false;
  }

  @Override
  public boolean delete(@NotNull Location location) {
    return false;
  }

  @Override
  public @Nullable BlockBinding.Record load(Location location) {
    return null;
  }

  @Override
  public List<Record> loadAllByChunk(Chunk chunk) {
    return List.of();
  }
}
