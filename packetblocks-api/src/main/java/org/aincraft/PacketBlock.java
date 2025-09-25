package org.aincraft;

import java.util.Optional;
import net.kyori.adventure.key.Keyed;

public interface PacketBlock {

  BlockModel model();

  PacketBlockMeta getMeta();

  interface PacketBlockMeta extends Keyed {

    BlockItemMeta blockItemMeta();

    BlockModel.BlockModelData blockModelData();

    Optional<SoundEntry> getSoundEntry(SoundType type);
  }
}
