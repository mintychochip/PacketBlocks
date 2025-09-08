package org.aincraft.domain;

import com.google.inject.Inject;
import org.aincraft.BlockBindingImpl;
import org.aincraft.Mapper;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.ClientBlockData;
import org.aincraft.api.ClientBlockData.Record;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public final class BlockBindingMapperImpl implements Mapper<BlockBinding, BlockBinding.Record> {

  private final Mapper<ClientBlockData, ClientBlockData.Record> blockDataInstanceMapper;

  @Inject
  public BlockBindingMapperImpl(
      Mapper<ClientBlockData, ClientBlockData.Record> blockDataInstanceMapper) {
    this.blockDataInstanceMapper = blockDataInstanceMapper;
  }

  @Override
  public @NotNull BlockBinding asDomain(@NotNull BlockBinding.Record record)
      throws IllegalArgumentException {
    ClientBlockData blockData = blockDataInstanceMapper.asDomain(
        record.blockData());
    World world = Bukkit.getWorld(record.world());
    if (world == null) {
      throw new IllegalArgumentException("failed to locate world");
    }
    Location location = new Location(world, record.x(), record.y(), record.z());
    return new BlockBindingImpl(blockData, location);
  }

  @Override
  public @NotNull BlockBinding.Record asRecord(@NotNull BlockBinding domain)
      throws IllegalArgumentException {
    Location location = domain.location();
    World world = location.getWorld();
    Chunk chunk = location.getChunk();
    Record record = blockDataInstanceMapper.asRecord(domain.blockData());
    return new BlockBinding.Record(world.getName(), location.getX(), location.getY(),
        location.getZ(), chunk.getX(), chunk.getZ(), record);
  }
}
