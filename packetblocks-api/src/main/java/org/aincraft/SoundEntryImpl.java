package org.aincraft;

import org.bukkit.Location;
import org.bukkit.World;

record SoundEntryImpl(String soundKey, float volume, float pitch) implements SoundEntry {

  @Override
  public void play(Location location) {
    World world = location.getWorld();
    world.playSound(location, soundKey, volume, pitch);
  }
}
