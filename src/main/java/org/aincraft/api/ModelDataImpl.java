package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.joml.Quaternionf;
import org.joml.Vector3f;

record ModelDataImpl(Key itemModel, Vector3f scale, Vector3f translation, Quaternionf rotation,
                     float range) implements ModelData {

  @Override
  public ModelDataBuilder toBuilder() {
    return new BuilderImpl()
        .itemModel(itemModel)
        .translation(new Vector3f(translation))
        .scale(new Vector3f(scale))
        .rotation(new Quaternionf(rotation))
        .range(range);
  }

  static final class BuilderImpl implements ModelData.ModelDataBuilder {

    private Key itemModel;
    private Vector3f scale, translation;
    private Quaternionf rotation;
    private float range;

    @Override
    public ModelDataBuilder itemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    @Override
    public ModelDataBuilder scale(Vector3f scale) {
      this.scale = scale;
      return this;
    }

    @Override
    public ModelDataBuilder translation(Vector3f translation) {
      this.translation = translation;
      return this;
    }

    @Override
    public ModelDataBuilder rotation(Quaternionf rotation) {
      this.rotation = rotation;
      return this;
    }

    @Override
    public ModelDataBuilder range(float range) {
      this.range = range;
      return this;
    }

    @Override
    public ModelData build() {
      return new ModelDataImpl(itemModel, scale, translation, rotation, range);
    }
  }
}
