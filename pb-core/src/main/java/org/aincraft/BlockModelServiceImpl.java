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
  private final Map<Location, EntityModel> modelMap = new HashMap<>();

  @Inject
  BlockModelServiceImpl(
      Registry<PacketBlockMeta> blockMetaRegistry) {
    this.blockMetaRegistry = blockMetaRegistry;
  }


  @Override
  public boolean save(BlockBinding blockBinding) {
    EntityModel newModel = blockFromBinding(blockBinding);
    EntityModel oldModel = modelMap.remove(blockBinding.location());
    if (oldModel != null) {
      oldModel.getViewers().forEach(oldModel::hideFrom);
    }
    modelMap.put(blockBinding.location(), newModel);
    return true;
  }

  @Override
  public @Nullable EntityModel load(Location location) {
    return modelMap.get(location);
  }

  @Override
  public boolean delete(Location location) {
    EntityModel model = modelMap.remove(location);
    if (model != null) {
      model.getViewers().forEach(model::hideFrom);
    }
    return true;
  }

  private EntityModel blockFromBinding(BlockBinding blockBinding) {
    PacketBlockMeta blockMeta = blockMetaRegistry.get(
        NamespacedKey.fromString(blockBinding.resourceKey()));
    Location location = blockBinding.location();
    EntityModel model = EntityModel.create(EntityType.ITEM_DISPLAY,
        blockBinding.location());
    model.setData(blockMeta.getEntityModelData());
    return model;
  }
}
