package org.aincraft;

import java.util.Set;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

@AvailableSince("1.0.4")
public interface BlockModel {

  @NotNull
  @AvailableSince("1.0.4")
  static BlockModel create(Location location, BlockModelData blockModelData) {
    return Bridge.bridge().packetBlockFactory().create(location, blockModelData);
  }

  boolean visible(Player player);

  void show(Player player);

  void hide(Player player);

  void move(Location location);

  void teleport(Location location);

  Set<Player> viewers();

  void data(BlockModelData blockModelData);

  BlockModelData data();

}
