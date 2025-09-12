package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.api.SoundData.SoundType;
import org.bukkit.entity.Player;

public interface SoundEntry {

  SoundType soundType();

  Key soundKey();

  float volume();

  float pitch();

  void play(Player player);

  record Record(String soundType, String soundKey, float volume, float pitch) {

  }
}
