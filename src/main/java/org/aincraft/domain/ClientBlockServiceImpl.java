package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.BlockModelImpl;
import org.aincraft.Mapper;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockBinding.Record;
import org.aincraft.api.BlockModel;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ClientBlockServiceImpl implements ClientBlockService {

  private final BlockBindingRepository blockBindingRepository;
  private final Mapper<BlockBinding, BlockBinding.Record> blockBindingMapper;
  private final Map<Location, BlockModel> blockMap = new HashMap<>();

  @Inject
  ClientBlockServiceImpl(BlockBindingRepository blockBindingRepository,
      Mapper<BlockBinding, Record> blockBindingMapper) {
    this.blockBindingRepository = blockBindingRepository;
    this.blockBindingMapper = blockBindingMapper;
  }

  @Override
  public @NotNull BlockModel upsertBlock(BlockBinding binding) {
    blockBindingRepository.save(blockBindingMapper.asRecord(binding));
    BlockModel newBlock = blockFromBinding(binding);
    BlockModel oldBlock = blockMap.remove(binding.location());
    if (oldBlock != null) {
      oldBlock.viewers().forEach(oldBlock::hide);
    }
    blockMap.put(binding.location(),newBlock);
    return newBlock;
  }

  @Override
  @Nullable
  public BlockModel loadBlock(Location location) {
    if (blockMap.containsKey(location)) {
      return blockMap.get(location);
    }
    Record record = blockBindingRepository.load(location);
    if (record == null) {
      return null;
    }
    BlockBinding blockBinding = blockBindingMapper.asDomain(record);
    BlockModel block = blockFromBinding(blockBinding);
    blockMap.put(location,
        block);
    return block;
  }

  @Override
  public boolean deleteBlock(@NotNull Location location) {
    if (!blockBindingRepository.delete(location)) {
      return false;
    }
    BlockModel block = blockMap.remove(location);
    block.viewers().forEach(block::hide);
    return true;
  }

  private static BlockModel blockFromBinding(BlockBinding blockBinding) {
    Location location = blockBinding.location();
    return BlockModelImpl.create(blockBinding.blockData(), location.getWorld(),
        location.toVector());
  }
}
