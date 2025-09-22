package org.aincraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketBlockMetaImpl implements PacketBlockMeta {

  private final @NotNull Key key;
  private final @NotNull BlockItemMeta blockItemMeta;
  private final @NotNull EntityModelData entityModelData;
  private final @NotNull Map<@NotNull SoundType, @NotNull SoundEntry> entries = new HashMap<>();

  public PacketBlockMetaImpl(
      final @NotNull Key key,
      final @NotNull BlockItemMeta blockItemMeta,
      final @NotNull EntityModelData entityModelData
  ) {
    this.key = key;
    this.blockItemMeta = blockItemMeta;
    this.entityModelData = entityModelData;
  }

  @Override
  public @NotNull BlockItemMeta getBlockItemMeta() {
    return blockItemMeta;
  }

  @Override
  public @NotNull EntityModelData getEntityModelData() {
    return entityModelData;
  }

  @Override
  public @NotNull Optional<SoundEntry> getSoundEntry(final @NotNull SoundType type) {
    return Optional.ofNullable(entries.get(type));
  }

  @Override
  public @NotNull Key key() {
    return key;
  }
}
