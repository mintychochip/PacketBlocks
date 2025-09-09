package org.aincraft.domain;

import net.kyori.adventure.key.Key;
import org.aincraft.api.SoundData.SoundType;
import org.aincraft.api.SoundEntry;
import org.bukkit.entity.Player;

public record SoundEntryImpl(Key resourceKey, SoundType type, Key soundKey, float volume,
                             float pitch) implements
    SoundEntry {

  @Override
  public void play(Player player) {
  }
}
