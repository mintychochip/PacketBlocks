package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.api.SoundData.SoundType;
import org.aincraft.domain.Repository;
import org.bukkit.entity.Player;

public interface SoundEntry {

  Key resourceKey();

  SoundType type();

  Key soundKey();

  float volume();

  float pitch();

  void play(Player player);

  record Record(String resourceKey, String soundType, String soundKey, float volume,
                float pitch) implements Repository.Record<RecordKey> {

    @Override
    public RecordKey key() {
      return new RecordKey(resourceKey, soundType);
    }
  }

  record RecordKey(String resourceKey, String soundType) {

  }
}
