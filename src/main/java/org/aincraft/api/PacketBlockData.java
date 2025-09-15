package org.aincraft.api;

import java.util.List;
import net.kyori.adventure.key.Key;
import org.aincraft.loot.LootData;
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

  @NotNull
  LootData lootData();

  @NotNull
  List<String> tags();
}
