package org.aincraft;

import java.util.Map;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.jetbrains.annotations.NotNull;

record PacketBlockMetaImpl(Key key, BlockItemMeta blockItemMeta, BlockModel.BlockModelData blockModelData,
                           Map<@NotNull SoundType, @NotNull SoundEntry> soundEntries) implements
    PacketBlockMeta {

  @Override
  public Optional<SoundEntry> getSoundEntry(SoundType type) {
    return Optional.ofNullable(soundEntries.get(type));
  }
}