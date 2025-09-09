package org.aincraft.domain;

import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.SoundData.SoundType;
import org.aincraft.api.SoundEntry;
import org.aincraft.api.SoundEntry.Record;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public final class SoundEntryMapperImpl implements Mapper<SoundEntry, SoundEntry.Record> {

  @Override
  public @NotNull SoundEntry asDomain(@NotNull SoundEntry.Record record)
      throws IllegalArgumentException {
    Key resourceKey = NamespacedKey.fromString(record.resourceKey());
    SoundType soundType = SoundType.valueOf(record.soundType());
    Key soundkey = NamespacedKey.fromString(record.soundKey());
    return new SoundEntryImpl(resourceKey, soundType, soundkey, record.volume(), record.pitch());
  }

  @Override
  public @NotNull SoundEntry.Record asRecord(@NotNull SoundEntry domain)
      throws IllegalArgumentException {
    String resourceKey = domain.resourceKey().toString();
    String soundType = domain.type().toString();
    String soundKey = domain.soundKey().toString();
    return new Record(resourceKey, soundType, soundKey, domain.volume(), domain.pitch());
  }
}
