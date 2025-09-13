package org.aincraft.api;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public final class SoundDataImpl implements SoundData {

  private final Map<SoundType, SoundEntry> entries;

  private SoundDataImpl(Map<SoundType, SoundEntry> entries) {
    EnumMap<SoundType, SoundEntry> map = new EnumMap<>(SoundType.class);
    if (entries != null && !entries.isEmpty()) {
      map.putAll(entries);
    }
    this.entries = Collections.unmodifiableMap(map);
  }

  @Override
  public Map<SoundType, SoundEntry> entries() {
    return entries;
  }

  @Override
  public @Nullable SoundEntry entry(SoundType type) {
    return entries.get(type);
  }

  @Override
  public SoundDataBuilder toBuilder() {
    return new BuilderImpl().entries(entries);
  }

  static final class BuilderImpl implements SoundDataBuilder {

    private final Map<SoundType, SoundEntry> entries = new EnumMap<>(SoundType.class);

    @Override
    public SoundDataBuilder entry(SoundType type, SoundEntry entry) {
      this.entries.put(type, entry);
      return this;
    }

    @Override
    public SoundDataBuilder entries(Map<SoundType, SoundEntry> entries) {
      this.entries.putAll(entries);
      return this;
    }

    @Override
    public SoundData build() {
      return new SoundDataImpl(entries);
    }
  }
}
