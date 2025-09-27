package org.aincraft;

import java.util.Optional;
import net.kyori.adventure.key.Keyed;

public interface PacketBlock {

  BlockModel model();

  BlockModelData blockModelData();

  interface PacketBlockMeta extends Keyed {

    BlockItemMeta blockItemMeta();

    BlockModelData blockModelData();

    Optional<SoundEntry> getSoundEntry(SoundType type);
  }
}
