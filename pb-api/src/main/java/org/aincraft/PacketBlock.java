package org.aincraft;

import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;

public interface PacketBlock {

  EntityModel getModel();

  String resourceKey();

  interface PacketBlockMeta {

    String resourceKey();

    BlockItemMeta getBlockItemMeta();

    EntityModelData entityModelMeta();

    Optional<SoundEntry> getSoundEntry(SoundType type);
  }
}
