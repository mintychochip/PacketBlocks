package org.aincraft.api;

import java.util.Map;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;

public interface SoundData {

  enum SoundType {
    BREAK,
    PLACE,
  }

  SoundEntry getEntry(SoundType type);

  record Record(String resourceKey, Map<String, SoundEntry.Record> entries) {

  }

  interface SoundEntry {

    SoundType type();

    Key soundKey();

    float volume();

    float pitch();

    void play(Player player);

    record Record(String soundType, String soundKey, float volume, float pitch) {

    }
  }
}
