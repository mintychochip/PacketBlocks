package org.aincraft.api;

import java.util.Map;
import org.jetbrains.annotations.Nullable;

public interface SoundData {

  enum SoundType {
    BREAK,
    PLACE
  }

  Map<SoundType,SoundEntry> entries();

  @Nullable
  default SoundEntry entry(SoundType type) {
    return entries().get(type);
  }
  record Record(Map<String,SoundEntry.Record> entries) {

  }

}
