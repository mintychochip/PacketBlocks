package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.EntityModel;
import org.aincraft.api.EntityModel.EntityModelMeta;
import org.aincraft.api.EntityModelAttributes;
import org.aincraft.api.PacketBlockData;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

final class BlockModelServiceImpl implements BlockModelService {

  private final PacketBlockDataRepository blockDataRepository;
  private final Map<Location, EntityModel> modelMap = new HashMap<>();

  @Inject
  BlockModelServiceImpl(PacketBlockDataRepository blockDataRepository) {
    this.blockDataRepository = blockDataRepository;
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
    PacketBlockData blockData = blockDataRepository.load(blockBinding.resourceKey());
    Location location = blockBinding.location();
    EntityModel model = EntityModel.create(EntityType.SHULKER,
        blockBinding.location());
    EntityModelMeta meta = model.getMeta();
    meta.setAttribute(EntityModelAttributes.INVISIBLE,true);
    meta.setAttribute(EntityModelAttributes.GLOWING,true);
    meta.setAttribute(EntityModelAttributes.GLOW_COLOR_OVERRIDE,1);
    model.setMeta(meta);
    return model;
  }
}
