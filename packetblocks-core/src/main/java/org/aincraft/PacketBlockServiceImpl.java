package org.aincraft;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.Registry;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

final class PacketBlockServiceImpl implements PacketBlockService {

  private final BlockModelService blockModelService;
  private final Registry<BlockModelData> blockModelDataRegistry;
  private final BlockBindingRepository blockBindingRepository;

  @Inject
  public PacketBlockServiceImpl(
      BlockModelService blockModelService, Registry<BlockModelData> blockModelDataRegistry,
      BlockBindingRepository blockBindingRepository) {
    this.blockModelService = blockModelService;
    this.blockModelDataRegistry = blockModelDataRegistry;
    this.blockBindingRepository = blockBindingRepository;
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
      public BlockModel model() {
        return blockModelService.load(blockBinding.location());
      }

      @Override
      public BlockModelData blockModelData() {
        return blockModelDataRegistry.get(NamespacedKey.fromString(blockBinding.resourceKey()));
      }


    };
  }

  @Override
  public @Nullable PacketBlock load(Location location) {
    BlockModel model = blockModelService.load(location);
    if (model == null) {
      return null;
    }
    BlockBinding binding = blockBindingRepository.load(location);
    if (binding == null) {
      return null;
    }
    return new PacketBlock() {

      @Override
      public BlockModel model() {
        return model;
      }

      @Override
      public BlockModelData blockModelData() {
        return blockModelDataRegistry.get(NamespacedKey.fromString(binding.resourceKey()));
      }

    };
  }

  @Override
  public List<PacketBlock> loadAll(Chunk chunk) {
    List<BlockBinding> bindings = blockBindingRepository.loadAllByChunk(chunk);
    List<PacketBlock> blocks = new ArrayList<>();
    bindings.forEach(binding -> {
      BlockModel model = blockModelService.load(binding.location());
      blocks.add(new PacketBlock() {
        @Override
        public BlockModel model() {
          return model;
        }

        @Override
        public BlockModelData blockModelData() {
          return blockModelDataRegistry.get(NamespacedKey.fromString(binding.resourceKey()));
        }
      });
    });
    return blocks;
  }
}
