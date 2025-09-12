package org.aincraft.sound;

import org.aincraft.ConnectionSource;
import org.aincraft.domain.Repository;
import org.jetbrains.annotations.Nullable;

public final class RelationalSoundDataRepositoryImpl implements
    Repository<String, Record> {

  private final Repository<SoundEntry.RecordKey, SoundEntry.Record> soundEntryRepository;
  private final ConnectionSource connectionSource;

  public RelationalSoundDataRepositoryImpl(
      Repository<SoundEntry.RecordKey, SoundEntry.Record> soundEntryRepository,
      ConnectionSource connectionSource) {
    this.soundEntryRepository = soundEntryRepository;
    this.connectionSource = connectionSource;
  }

  @Override
  public @Nullable SoundData.Record load(String key) {
    return null;
  }
}
