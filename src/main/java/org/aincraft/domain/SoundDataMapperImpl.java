package org.aincraft.domain;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.SoundData;
import org.aincraft.api.SoundEntry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundDataMapperImpl implements Mapper<SoundData, SoundData.Record> {

  @Override
  public @NotNull SoundData asDomain(@NotNull SoundData.Record record)
      throws IllegalArgumentException {
    // Map record entries (String → SoundEntry.Record) to domain entries (SoundType → SoundEntry)
    Map<SoundData.SoundType, SoundEntry> domainEntries = new EnumMap<>(SoundData.SoundType.class);

    for (SoundEntry.Record r : record.entries().values()) {
      SoundData.SoundType type;
      try {
        type = SoundData.SoundType.valueOf(r.soundType().toUpperCase());
      } catch (IllegalArgumentException ex) {
        throw new IllegalArgumentException("Unknown SoundType: " + r.soundType(), ex);
      }

      SoundEntry entry = new DomainSoundEntry(
          Key.key(r.resourceKey()),
          type,
          Key.key(r.soundKey()),
          r.volume(),
          r.pitch()
      );
      domainEntries.put(type, entry);
    }

    return new DomainSoundData(record.resourceKey(), domainEntries);
  }

  @Override
  public @NotNull SoundData.Record asRecord(@NotNull SoundData domain)
      throws IllegalArgumentException {
    Map<String, SoundEntry.Record> entries = new HashMap<>();
    String resourceKeyFromEntries = null;

    for (SoundData.SoundType type : SoundData.SoundType.values()) {
      SoundEntry e = domain.getEntry(type);
      if (e == null) {
        continue;
      }

      String rk = e.resourceKey().asString();
      if (resourceKeyFromEntries == null) {
        resourceKeyFromEntries = rk;
      } else if (!resourceKeyFromEntries.equals(rk)) {
        throw new IllegalArgumentException(
            "Inconsistent resourceKey across entries: " + resourceKeyFromEntries + " vs " + rk
        );
      }

      entries.put(
          type.name().toLowerCase(),
          new SoundEntry.Record(
              rk,
              type.name().toLowerCase(),
              e.soundKey().asString(),
              e.volume(),
              e.pitch()
          )
      );
    }

    if (resourceKeyFromEntries == null) {
      throw new IllegalArgumentException("SoundData has no entries; cannot infer resourceKey.");
    }

    return new SoundData.Record(resourceKeyFromEntries, entries);
  }

  // ----------------- small domain adapters -----------------

  private static final class DomainSoundData implements SoundData {

    private final String resourceKey;
    private final Map<SoundData.SoundType, SoundEntry> entries;

    DomainSoundData(String resourceKey, Map<SoundData.SoundType, SoundEntry> entries) {
      this.resourceKey = resourceKey;
      this.entries = Map.copyOf(entries);
    }

    @Override
    public SoundEntry getEntry(SoundData.SoundType type) {
      return entries.get(type);
    }
  }

  private static final class DomainSoundEntry implements SoundEntry {

    private final Key resourceKey;
    private final SoundData.SoundType type;
    private final Key soundKey;
    private final float volume;
    private final float pitch;

    DomainSoundEntry(Key resourceKey, SoundData.SoundType type, Key soundKey, float volume,
        float pitch) {
      this.resourceKey = resourceKey;
      this.type = type;
      this.soundKey = soundKey;
      this.volume = volume;
      this.pitch = pitch;
    }

    @Override
    public Key resourceKey() {
      return resourceKey;
    }

    @Override
    public SoundData.SoundType type() {
      return type;
    }

    @Override
    public Key soundKey() {
      return soundKey;
    }

    @Override
    public float volume() {
      return volume;
    }

    @Override
    public float pitch() {
      return pitch;
    }

    @Override
    public void play(Player player) {
      // String-based overload supports custom namespaced keys
      player.playSound(player.getLocation(), soundKey.asString(), volume, pitch);
    }
  }
}
