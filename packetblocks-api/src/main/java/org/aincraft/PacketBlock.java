package org.aincraft;

import java.util.Map;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;

public interface PacketBlock {

  EntityModel getModel();

  PacketBlockMeta getMeta();

  interface PacketBlockMeta extends Keyed {

    BlockItemMeta getBlockItemMeta();

    EntityModelData getEntityModelData ();

    Optional<SoundEntry> getSoundEntry(SoundType type);
  }
}
