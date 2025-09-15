package org.aincraft.domain;

import com.google.inject.Inject;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.aincraft.api.PacketBlock;
import org.aincraft.api.PacketBlockData;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

final class PacketBlockServiceImpl implements PacketBlockService {

  private final BlockModelService blockModelService;
  private final PacketBlockDataRepository blockDataRepository;
  private final BlockBindingRepository blockBindingRepository;

  @Inject
  public PacketBlockServiceImpl(
      BlockModelService blockModelService,
      BlockBindingRepository blockBindingRepository,
      PacketBlockDataRepository blockDataRepository) {
    this.blockModelService = blockModelService;
    this.blockBindingRepository = blockBindingRepository;
    this.blockDataRepository = blockDataRepository;
  }

  @Override
  public boolean delete(Location location) {
    if (!blockBindingRepository.delete(location)) {
      return false;
    }
    return blockModelService.delete(location);
  }

  @Override
  public PacketBlock save(BlockBinding blockBinding) {
    blockBindingRepository.save(blockBinding);
    blockModelService.save(blockBinding);
    return new PacketBlock() {
      @Override
      public BlockModel blockModel() {
        return blockModelService.load(blockBinding.location());
      }

      @Override
      public PacketBlockData blockData() {
        return blockDataRepository.load(blockBinding.resourceKey().toString());
      }
    };
  }

  @Override
  public @Nullable PacketBlock load(Location location) {
    BlockModel model = blockModelService.load(location);
    BlockBinding blockBinding = blockBindingRepository.load(location);
    if (model == null && blockBinding != null) {
      blockModelService.save(blockBinding);
      model = blockModelService.load(location);
    }
    BlockModel finalModel = model;
    return new PacketBlock() {
      @Override
      public BlockModel blockModel() {
        return finalModel;
      }

      @Override
      public PacketBlockData blockData() {
        return blockDataRepository.load(blockBinding.resourceKey().toString());
      }
    };
  }
}
