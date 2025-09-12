package org.aincraft.domain;

import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.ModelData;
import org.aincraft.api.ModelDataRecord;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class ModelDataMapperImpl implements
    Mapper<ModelData, ModelDataRecord> {

  @Override
  public @NotNull ModelData asDomain(@NotNull ModelDataRecord modelDataRecord) {
    Key itemModel = NamespacedKey.fromString(modelDataRecord.itemModel());
    Vector3f translation = new Vector3f(modelDataRecord.tx(), modelDataRecord.ty(), modelDataRecord.tz());
    Quaternionf leftRotation = new Quaternionf(modelDataRecord.lx(), modelDataRecord.ly(), modelDataRecord.lz(), modelDataRecord.lw());
    Vector3f scale = new Vector3f(modelDataRecord.sx(), modelDataRecord.sy(), modelDataRecord.sz());
    Quaternionf rightRotation = new Quaternionf(modelDataRecord.rx(), modelDataRecord.ry(), modelDataRecord.rz(), modelDataRecord.rw());
    return new ModelDataImpl(resourceKey, itemModel, translation,
        leftRotation, scale,
        rightRotation, modelDataRecord.range(),
        ModelDataImpl.asNMSBrightness(modelDataRecord.block(), modelDataRecord.sky()));
  }

  @Override
  public @NotNull ModelDataRecord asRecord(@NotNull ModelData domain) {
    Vector3f translation = domain.translation();
    Quaternionf rotation = domain.rotation();
    float scale = domain.scale();
    Quaternionf rightRotation = domain.rightRotation();
    return new ModelDataRecord(domain.resourceKey().toString(),
        domain.itemModel().toString(), translation.x(),
        translation.y(), translation.z(), leftRotation.x(), leftRotation.y(), leftRotation.z(),
        leftRotation.w(), scale.x(), scale.y(), scale.z(), rightRotation.x(), rightRotation.y(),
        rightRotation.z(), rightRotation.w(), domain.range(), domain.blockLight(),
        domain.skyLight());
  }
}
