package org.aincraft.api;

import java.util.Map;
import org.aincraft.domain.Repository;

public interface SoundData {

  enum SoundType {
    BREAK,
    PLACE,
  }

  SoundEntry getEntry(SoundType type);

  record Record(String resourceKey, Map<String, SoundEntry.Record> entries) implements Repository

      .Record<String> {

    @Override
    public String key() {
      return resourceKey;
    }
  }

}
