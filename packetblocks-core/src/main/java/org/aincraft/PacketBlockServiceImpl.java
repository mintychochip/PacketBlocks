package org.aincraft;

import com.google.inject.Inject;
import java.util.List;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.Registry;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

final class PacketBlockServiceImpl implements PacketBlockService {

  private final BlockModelService blockModelService;
  private final Registry<PacketBlockMeta> blockMetaRegistry;
  private final BlockBindingRepository blockBindingRepository;

  @Inject
  public PacketBlockServiceImpl(
      BlockModelService blockModelService, Registry<PacketBlockMeta> blockMetaRegistry,
      BlockBindingRepository blockBindingRepository) {
    this.blockModelService = blockModelService;
    this.blockMetaRegistry = blockMetaRegistry;
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
      public EntityModel getModel() {
        return blockModelService.load(blockBinding.location());
      }

      @Override
      public PacketBlockMeta getMeta() {
        return blockMetaRegistry.get(NamespacedKey.fromString(blockBinding.resourceKey()));
      }
    };
  }

  @Override
  public @Nullable PacketBlock load(Location location) {
    EntityModel model = blockModelService.load(location);
    if (model == null) {
      return null;
    }
    BlockBinding binding = blockBindingRepository.load(location);
    if (binding == null) {
      return null;
    }
    return new PacketBlock() {

      @Override
      public EntityModel getModel() {
        return model;
      }

      @Override
      public PacketBlockMeta getMeta() {
        return blockMetaRegistry.get(NamespacedKey.fromString(binding.resourceKey()));
      }
    };
  }

  @Override
  public List<PacketBlock> loadAll(Chunk chunk) {
    List<BlockBinding> bindings = blockBindingRepository.loadAllByChunk(chunk);
    return List.of();
  }
}
