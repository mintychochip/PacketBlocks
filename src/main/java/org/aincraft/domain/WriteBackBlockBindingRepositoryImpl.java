package org.aincraft.domain;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockBinding.Record;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WriteBackBlockBindingRepositoryImpl extends
    WriteBackRepositoryImpl<Location, BlockBinding.Record> implements BlockBindingRepository {

  private static final WriteBackConfiguration CONFIGURATION = new WriteBackConfiguration(0L, 20L,
      50, TimeUnit.SECONDS);

  private WriteBackBlockBindingRepositoryImpl(Writable<Location, BlockBinding.Record> delegate,
      WriteBackConfiguration configuration) {
    super(delegate, configuration);
  }

  public static WriteBackBlockBindingRepositoryImpl create(BlockBindingRepository delegate,
      Plugin plugin) {
    WriteBackBlockBindingRepositoryImpl d = new WriteBackBlockBindingRepositoryImpl(delegate,
        CONFIGURATION);
    d.run(plugin);
    return d;
  }

  @Override
  public List<BlockBinding.Record> loadAllByChunk(Chunk chunk) {

    return List.of();
  }
}
