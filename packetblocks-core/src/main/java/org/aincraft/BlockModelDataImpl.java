package org.aincraft;

import net.kyori.adventure.key.Key;
import org.aincraft.EntityModelImpl.EntityModelDataImpl;
import org.joml.Quaternionf;
import org.joml.Vector3f;

record BlockModelDataImpl(EntityModelData entityModelData)
    implements BlockModel.BlockModelData {

  @Override
  public Vector3f translation() {
    return entityModelData.getAttribute(EntityModelAttributes.TRANSLATION);
  }

  @Override
  public Vector3f scale() {
    return entityModelData.getAttribute(EntityModelAttributes.SCALE);
  }

  @Override
  public Quaternionf leftRotation() {
    return entityModelData.getAttribute(EntityModelAttributes.LEFT_ROTATION);
  }

  @Override
  public Quaternionf rightRotation() {
    return entityModelData.getAttribute(EntityModelAttributes.RIGHT_ROTATION);
  }

  @Override
  public Key itemModel() {
    return entityModelData.getAttribute(EntityModelAttributes.ITEM_MODEL);
  }

  static final class BuilderImpl implements BlockModel.BlockModelData.Builder {
    private final EntityModelData data = EntityModelDataImpl.create();

    @Override
    public BlockModel.BlockModelData.Builder translation(Vector3f translation) {
      data.setAttribute(EntityModelAttributes.TRANSLATION, translation);
      return this;
    }

    @Override
    public BlockModel.BlockModelData.Builder scale(Vector3f scale) {
      data.setAttribute(EntityModelAttributes.SCALE, scale);
      return this;
    }

    @Override
    public BlockModel.BlockModelData.Builder leftRotation(Quaternionf leftRotation) {
      data.setAttribute(EntityModelAttributes.LEFT_ROTATION, leftRotation);
      return this;
    }

    @Override
    public BlockModel.BlockModelData.Builder rightRotation(Quaternionf rightRotation) {
      data.setAttribute(EntityModelAttributes.RIGHT_ROTATION, rightRotation);
      return this;
    }

    @Override
    public BlockModel.BlockModelData.Builder itemModel(Key itemModel) {
      data.setAttribute(EntityModelAttributes.ITEM_MODEL, itemModel);
      return this;
    }

    @Override
    public BlockModel.BlockModelData build() {
      return new BlockModelDataImpl(data);
    }
  }
}
