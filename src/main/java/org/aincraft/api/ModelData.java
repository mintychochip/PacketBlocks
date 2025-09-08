package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ModelData {

  Key resourceKey();

  Key itemModel();

  Vector3f scale();

  Vector3f translation();

  Quaternionf leftRotation();

  Quaternionf rightRotation();

  float range();

  int blockLight();

  int skyLight();

  ModelData range(float range);

  ModelData brightness(int block, int sky);

  ModelData scale(Vector3f scale);

  ModelData translation(Vector3f translation);

  ModelData leftRotation(Quaternionf leftRotation);

  ModelData rightRotation(Quaternionf rightRotation);

  ModelData itemModel(Key key);

  interface Builder {

    Builder setItemModel(Key itemModel);

    Builder setScale(Vector3f scale);

    Builder setTranslation(Vector3f translation);

    Builder setLeftRotation(Quaternionf leftRotation);

    Builder setRightRotation(Quaternionf rightRotation);

    Builder setRange(float range);

    Builder setBrightness(int block, int sky);

    ModelData build();
  }

  record Record(String resourceKey, String itemModel,
                float tx, float ty, float tz, float lx, float ly, float lz, float lw, float sx,
                float sy, float sz, float rx, float ry, float rz, float rw, float range,
                int block,
                int sky) {

  }
}
