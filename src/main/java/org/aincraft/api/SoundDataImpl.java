package org.aincraft.api;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class SoundDataImpl implements SoundData {

  private final Map<SoundType, SoundEntry> entries;

  private SoundDataImpl(Map<SoundType, SoundEntry> entries) {
    // Wrap in unmodifiable to prevent accidental mutation
    this.entries = Collections.unmodifiableMap(new EnumMap<>(entries));
  }

  @Override
  public Map<SoundType, SoundEntry> entries() {
    return entries;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private final Map<SoundType, SoundEntry> entries = new EnumMap<>(SoundType.class);

    public Builder entry(SoundType type, SoundEntry entry) {
      entries.put(type, entry);
      return this;
    }

    public Builder entries(Map<SoundType, SoundEntry> entries) {
      this.entries.putAll(entries);
      return this;
    }

    public SoundDataImpl build() {
      return new SoundDataImpl(entries);
    }
  }
}
