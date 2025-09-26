package org.aincraft;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@AvailableSince("1.0.4")
public interface BlockModelData {

  @NotNull
  @AvailableSince("1.0.4")
  static Builder builder() {
    return Bridge.bridge().packetBlockFactory().dataBuilder();
  }

  Vector3f translation();

  Vector3f scale();

  Quaternionf leftRotation();

  Quaternionf rightRotation();

  Key itemModel();

  interface Builder {

    Builder translation(Vector3f translation);

    Builder scale(Vector3f scale);

    Builder leftRotation(Quaternionf leftRotation);

    Builder rightRotation(Quaternionf rightRotation);

    Builder itemModel(Key itemModel);

    BlockModelData build();
  }
}
