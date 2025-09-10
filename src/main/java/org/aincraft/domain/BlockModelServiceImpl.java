package org.aincraft.domain;

import java.util.HashMap;
import java.util.Map;
import org.aincraft.BlockModelImpl;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

final class BlockModelServiceImpl implements BlockModelService {

  private final Map<Location, BlockModel> blockModelMap = new HashMap<>();

  @Override
  public boolean save(BlockBinding binding) {
    BlockModel newBlock = blockFromBinding(binding);
    BlockModel oldBlock = blockModelMap.remove(binding.location());
    if (oldBlock != null) {
      oldBlock.viewers().forEach(oldBlock::hide);
    };
    blockModelMap.put(binding.location(),newBlock);
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

  static BlockModel blockFromBinding(BlockBinding blockBinding) {
    Location location = blockBinding.location();
    return BlockModelImpl.create(blockBinding.blockData(), location.getWorld(),
        location.toVector());
  }
}
