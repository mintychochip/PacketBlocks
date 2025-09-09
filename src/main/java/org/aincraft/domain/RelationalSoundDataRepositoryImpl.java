package org.aincraft.domain;

import org.aincraft.ConnectionSource;
import org.aincraft.api.SoundData;
import org.jetbrains.annotations.Nullable;

public final class RelationalSoundDataRepositoryImpl implements
    Repository<String, SoundData.Record> {

  private final ConnectionSource connectionSource;

  public RelationalSoundDataRepositoryImpl(ConnectionSource connectionSource) {
    this.connectionSource = connectionSource;
  }

  @Override
  public @Nullable SoundData.Record load(String key) {
    return null;
  }
}
