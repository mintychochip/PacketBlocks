package org.aincraft;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.Registry;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

final class BlockModelServiceImpl implements BlockModelService {

  private final Registry<PacketBlockMeta> blockMetaRegistry;
  private final Map<Location, BlockModel> modelMap = new HashMap<>();

  @Inject
  BlockModelServiceImpl(
      Registry<PacketBlockMeta> blockMetaRegistry) {
    this.blockMetaRegistry = blockMetaRegistry;
  }


  @Override
  public boolean save(BlockBinding blockBinding) {
    BlockModel model = blockFromBinding(blockBinding);
    BlockModel oldModel = modelMap.remove(blockBinding.location());
    if (oldModel != null) {
      oldModel.viewers().forEach(oldModel::hide);
    }
    modelMap.put(blockBinding.location(), model);
    return true;
  }

  @Override
  public @Nullable BlockModel load(Location location) {
    return modelMap.get(location);
  }

  @Override
  public boolean delete(Location location) {
    BlockModel blockModel = modelMap.remove(location);
    if (blockModel != null) {
      blockModel.viewers().forEach(blockModel::hide);
    }
    return true;
  }

  private BlockModel blockFromBinding(BlockBinding blockBinding) {
    PacketBlockMeta blockMeta = blockMetaRegistry.get(
        NamespacedKey.fromString(blockBinding.resourceKey()));
    BlockModel model = BlockModel.create(blockBinding.location());
    model.data(blockMeta.blockModelData());
    return model;
  }
}
