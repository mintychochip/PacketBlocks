package org.aincraft;

import java.util.Set;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@AvailableSince("1.0.4")
public interface BlockModel {

  @NotNull
  @AvailableSince("1.0.4")
  static BlockModel create(Location location) {
    return Bridge.bridge().packetBlockFactory().create(location);
  }

  boolean visible(Player player);

  void show(Player player);

  void hide(Player player);

  void move(Location location);

  void teleport(Location location);

  Set<Player> viewers();

  void data(BlockModelData blockModelData);

  BlockModelData data();

  @AvailableSince("1.0.4")
  interface BlockModelData {

    @NotNull
    @AvailableSince("1.0.4")
    static BlockModelData.Builder builder() {
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
}
