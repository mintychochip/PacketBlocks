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

  private final BlockBindingRepository delegate;
  private static final WriteBackConfiguration CONFIGURATION = new WriteBackConfiguration(0L, 20L,
      50, TimeUnit.SECONDS);

  private WriteBackBlockBindingRepositoryImpl(
      WriteBackConfiguration configuration, BlockBindingRepository delegate) {
    super(configuration);
    this.delegate = delegate;
  }

  public static WriteBackBlockBindingRepositoryImpl create(BlockBindingRepository delegate,
      Plugin plugin) {
    WriteBackBlockBindingRepositoryImpl d = new WriteBackBlockBindingRepositoryImpl(CONFIGURATION,
        delegate);
    d.run(plugin);
    return d;
  }

  @Override
  public List<BlockBinding.Record> loadAllByChunk(Chunk chunk) {
    return delegate.loadAllByChunk(chunk);
  }

  @Override
  protected Writable<Location, BlockBinding.Record> delegate() {
    return delegate;
  }
}
