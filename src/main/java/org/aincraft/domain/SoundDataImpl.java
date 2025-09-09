package org.aincraft.domain;

import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.SoundData;
import org.aincraft.api.SoundEntry;

public final class SoundDataImpl implements SoundData {

  private final Map<SoundType, SoundEntry> entries = new HashMap<>();
  @Override
  public SoundEntry getEntry(SoundType type) {
    return entries.get(type);
  }
}