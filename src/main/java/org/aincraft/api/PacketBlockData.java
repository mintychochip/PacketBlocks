package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface PacketBlockData {

  @NotNull
  Key resourceKey();

  @NotNull
  ModelData modelData();

  @NotNull
  ItemData itemData();

  @NotNull
  SoundData soundData();
}
