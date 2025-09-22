package org.aincraft;

import java.util.Optional;
import net.kyori.adventure.key.Keyed;

public interface PacketBlock {

  EntityModel getModel();

  PacketBlockMeta getMeta();

  interface PacketBlockMeta extends Keyed {

    BlockItemMeta getBlockItemMeta();

    EntityModelData getEntityModelData();

    Optional<SoundEntry> getSoundEntry(SoundType type);
  }
}
