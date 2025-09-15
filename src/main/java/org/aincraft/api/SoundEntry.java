package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.api.SoundData.SoundType;
import org.bukkit.Location;

public interface SoundEntry {

  SoundType soundType();

  Key soundKey();

  float volume();

  float pitch();

  void play(Location location);

  record Record(String soundType, String soundKey, float volume, float pitch) {

  }
}
