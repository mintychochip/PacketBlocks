package org.aincraft;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.registry.Registry;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

final class BlockModelServiceImpl implements BlockModelService {

  private final Registry<BlockModelData> blockModelDataRegistry;
  private final Map<LocationKey, BlockModel> modelMap = new HashMap<>();

  @Inject
  BlockModelServiceImpl(
      Registry<BlockModelData> blockModelDataRegistry) {
    this.blockModelDataRegistry = blockModelDataRegistry;
  }


  @Override
  public boolean save(@NotNull BlockBinding blockBinding) {
    LocationKey locationKey = LocationKey.create(blockBinding.location());
    BlockModel model = blockFromBinding(blockBinding);
    BlockModel oldModel = modelMap.remove(locationKey);
    if (oldModel != null) {
      oldModel.viewers().forEach(oldModel::hide);
    }
    modelMap.put(locationKey, model);
    return true;
  }

  @Override
  public boolean isModelLoaded(@NotNull Location location) {
    return modelMap.containsKey(LocationKey.create(location));
  }

  @Override
  public @NotNull BlockModel loadModel(@NotNull Location location) throws IllegalArgumentException {
    Preconditions.checkArgument(isModelLoaded(location));
    return modelMap.get(LocationKey.create(location));
  }

  @Override
  public boolean delete(Location location) {
    LocationKey locationKey = LocationKey.create(location);
    BlockModel blockModel = modelMap.remove(locationKey);
    if (blockModel != null) {
      blockModel.viewers().forEach(blockModel::hide);
    }
    return true;
  }

  private BlockModel blockFromBinding(BlockBinding blockBinding) {
    BlockModelData modelData = blockModelDataRegistry.get(
        NamespacedKey.fromString(blockBinding.resourceKey()));
    return BlockModel.create(blockBinding.location(),modelData);
  }
}
