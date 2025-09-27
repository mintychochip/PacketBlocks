package org.aincraft;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@AvailableSince("1.0.4")
public interface BlockModelData extends Keyed {

  @NotNull
  @AvailableSince("1.0.4")
  static Builder builder(Key key) {
    return Bridge.bridge().packetBlockFactory().blockModelDataBuilder(key);
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
