package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.BlockModelImpl;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.aincraft.api.PacketBlockData;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

final class BlockModelServiceImpl implements BlockModelService {

  private final PacketBlockDataRepository blockDataRepository;
  private final Map<Location, BlockModel> blockModelMap = new HashMap<>();

  @Inject
  BlockModelServiceImpl(PacketBlockDataRepository blockDataRepository) {
    this.blockDataRepository = blockDataRepository;
  }


  @Override
  public boolean save(BlockBinding blockBinding) {
    BlockModel newBlock = blockFromBinding(blockBinding);
    BlockModel oldBlock = blockModelMap.remove(blockBinding.location());
    if (oldBlock != null) {
      oldBlock.viewers().forEach(oldBlock::hide);
    }
    ;
    blockModelMap.put(blockBinding.location(), newBlock);
    return true;
  }

  @Override
  public @Nullable BlockModel load(Location location) {
    return blockModelMap.get(location);
  }

  @Override
  public boolean delete(Location location) {
    BlockModel model = blockModelMap.remove(location);
    if (model != null) {
      model.viewers().forEach(model::hide);
    }
    return true;
  }

  private BlockModel blockFromBinding(BlockBinding blockBinding) {
    PacketBlockData blockData = blockDataRepository.load("item:bus");
    Location location = blockBinding.location();
    return BlockModelImpl.create(blockData.modelData(), location.getWorld(), location.toVector());
  }
}
