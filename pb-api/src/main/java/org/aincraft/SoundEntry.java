package org.aincraft;

import org.bukkit.Location;

public sealed interface SoundEntry permits SoundEntryImpl {

  void play(Location location);

}
