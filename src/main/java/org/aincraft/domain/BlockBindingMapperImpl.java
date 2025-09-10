package org.aincraft.domain;

import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import org.aincraft.BlockBindingImpl;
import org.aincraft.Mapper;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.ModelData;
import org.aincraft.api.ModelData.Record;
import org.aincraft.api.SoundData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public final class BlockBindingMapperImpl implements Mapper<BlockBinding, BlockBinding.Record> {

  private final Mapper<ModelData, ModelData.Record> blockDataInstanceMapper;

  @Inject
  public BlockBindingMapperImpl(
      Mapper<ModelData, ModelData.Record> blockDataInstanceMapper) {
    this.blockDataInstanceMapper = blockDataInstanceMapper;
  }

  @Override
  public @NotNull BlockBinding asDomain(@NotNull BlockBinding.Record record)
      throws IllegalArgumentException {
    ModelData blockData = blockDataInstanceMapper.asDomain(
        record.blockData());
    World world = Bukkit.getWorld(record.world());
    if (world == null) {
      throw new IllegalArgumentException("failed to locate world");
    }
    Location location = new Location(world, record.x(), record.y(), record.z());
    Key resourceKey = NamespacedKey.fromString(record.resourceKey());
    return new BlockBindingImpl(resourceKey, blockData, location);
  }

  @Override
  public @NotNull BlockBinding.Record asRecord(@NotNull BlockBinding domain)
      throws IllegalArgumentException {
    Location location = domain.location();
    World world = location.getWorld();
    Chunk chunk = location.getChunk();
    Record record = blockDataInstanceMapper.asRecord(domain.blockData());
    return new BlockBinding.Record(world.getName(), location.getX(), location.getY(),
        location.getZ(), chunk.getX(), chunk.getZ(), domain.resourceKey().toString(), record);
  }
}
