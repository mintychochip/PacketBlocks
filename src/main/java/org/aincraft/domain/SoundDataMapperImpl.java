package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.util.Map.Entry;
import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.SoundData;
import org.aincraft.api.SoundData.SoundType;
import org.aincraft.api.SoundEntry;
import org.aincraft.api.SoundEntry.Record;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class SoundDataMapperImpl implements Mapper<SoundData, SoundData.Record> {

  private final Mapper<SoundEntry, SoundEntry.Record> soundEntryMapper;

  @Inject
  SoundDataMapperImpl(Mapper<SoundEntry, SoundEntry.Record> soundEntryMapper) {
    this.soundEntryMapper = soundEntryMapper;
  }

  @Override
  public @NotNull SoundData asDomain(@NotNull SoundData.Record record)
      throws IllegalArgumentException {
    Map<SoundType, SoundEntry> entries = new HashMap<>();
    record.entries().forEach((key, value) -> {
      entries.put(SoundType.valueOf(key.toUpperCase(Locale.ENGLISH)),
          soundEntryMapper.asDomain(value));
    });
    return () -> entries;
  }

  @Override
  public @NotNull SoundData.Record asRecord(@NotNull SoundData domain)
      throws IllegalArgumentException {
    Map<String, SoundEntry.Record> records = new HashMap<>();
    domain.entries().forEach((key, value) -> {
      String soundType = value.soundType().toString().toLowerCase(Locale.ENGLISH);
      String soundKey = value.soundKey().toString();
      records.put(key.toString().toLowerCase(Locale.ENGLISH),
          new SoundEntry.Record(soundType, soundKey, value.volume(),
              value.pitch()));
    });
    return new SoundData.Record(records);
  }

  static final class SoundEntryMapperImpl implements Mapper<SoundEntry, SoundEntry.Record> {

    @Override
    public @NotNull SoundEntry asDomain(@NotNull SoundEntry.Record record)
        throws IllegalArgumentException {
      SoundType soundType = SoundType.valueOf(record.soundType().toUpperCase(Locale.ENGLISH));
      Key soundKey = NamespacedKey.fromString(record.soundKey());
      return new SoundEntryImpl(soundType, soundKey, record.volume(), record.pitch());
    }

    @Override
    public @NotNull SoundEntry.Record asRecord(@NotNull SoundEntry domain)
        throws IllegalArgumentException {
      String soundType = domain.soundType().toString().toLowerCase(Locale.ENGLISH);
      String soundKey = domain.soundKey().toString();
      return new Record(soundType, soundKey, domain.volume(), domain.pitch());
    }
  }

  record SoundEntryImpl(SoundType soundType, Key soundKey, float volume, float pitch) implements
      SoundEntry {

    @Override
    public void play(Player player) {

    }
  }
}
