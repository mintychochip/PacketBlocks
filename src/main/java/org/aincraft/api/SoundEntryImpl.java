package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.api.SoundData.SoundType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public record SoundEntryImpl(SoundType soundType, Key soundKey, float volume,
                             float pitch) implements SoundEntry {

  @Override
  public void play(Location location) {
    World world = location.getWorld();
    world.playSound(location, soundKey.toString(), volume, pitch);
  }
}
