package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.api.Builder.Buildable;
import org.aincraft.api.ModelData.ModelDataBuilder;
import org.aincraft.api.ModelDataImpl.BuilderImpl;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ModelData extends Buildable<ModelDataBuilder, ModelData> {

  static ModelDataBuilder builder() {
    return new BuilderImpl();
  }

  Key itemModel();

  Vector3f scale();

  Vector3f translation();

  Quaternionf rotation();

  float range();

  interface ModelDataBuilder extends org.aincraft.api.Builder<ModelData> {

    ModelDataBuilder itemModel(Key itemModel);

    ModelDataBuilder scale(Vector3f scale);

    ModelDataBuilder translation(Vector3f translation);

    ModelDataBuilder rotation(Quaternionf rotation);

    ModelDataBuilder range(float range);

    ModelData build();
  }
}
