package org.aincraft.domain;

import java.util.Map;
import org.aincraft.api.SoundData;
import org.jetbrains.annotations.Nullable;

public class SoundDataRepositoryImpl implements Repository<String, SoundData.Record> {

  private final Map<String, SoundData.Record> soundDataRecord;

  public SoundDataRepositoryImpl(Map<String, SoundData.Record> soundDataRecord) {
    this.soundDataRecord = soundDataRecord;
  }

  @Override
  public @Nullable SoundData.Record load(String key) {
    return soundDataRecord.get(key);
  }
}
