package org.aincraft.api;

import java.util.UUID;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ClientBlockData {

  Key resourceKey();

  Key itemModel();

  Vector3f scale();

  Vector3f translation();

  Quaternionf leftRotation();

  Quaternionf rightRotation();

  float range();

  int blockLight();

  int skyLight();

  ClientBlockData range(float range);

  ClientBlockData brightness(int block, int sky);

  ClientBlockData scale(Vector3f scale);

  ClientBlockData translation(Vector3f translation);

  ClientBlockData leftRotation(Quaternionf leftRotation);

  ClientBlockData rightRotation(Quaternionf rightRotation);

  ClientBlockData itemModel(Key key);

  interface Builder {

    Builder setItemModel(Key itemModel);

    Builder setScale(Vector3f scale);

    Builder setTranslation(Vector3f translation);

    Builder setLeftRotation(Quaternionf leftRotation);

    Builder setRightRotation(Quaternionf rightRotation);

    Builder setRange(float range);

    Builder setBrightness(int block, int sky);

    ClientBlockData build();
  }

  record Record(String resourceKey, String itemModel,
                float tx, float ty, float tz, float lx, float ly, float lz, float lw, float sx,
                float sy, float sz, float rx, float ry, float rz, float rw, float range,
                int block,
                int sky) {

  }
}
