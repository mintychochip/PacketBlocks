package org.aincraft.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.aincraft.ConnectionSource;
import org.aincraft.api.SoundEntry;
import org.aincraft.api.SoundEntry.RecordKey;
import org.jetbrains.annotations.Nullable;

public final class RelationalSoundEntryRepositoryImpl implements
    Repository<SoundEntry.RecordKey, SoundEntry.Record> {

  private final ConnectionSource connectionSource;

  public RelationalSoundEntryRepositoryImpl(ConnectionSource connectionSource) {
    this.connectionSource = connectionSource;
  }

  @Override
  public @Nullable SoundEntry.Record load(RecordKey key) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT sound_key,volume,pitch FROM sound_entries WHERE resource_key = ? AND sound_type = ?")) {
      ps.setString(1, key.resourceKey());
      ps.setString(2, key.soundType());
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          return null;
        }
        String soundKey = rs.getString("sound_key");
        float volume = rs.getFloat("volume");
        float pitch = rs.getFloat("pitch");
        return new SoundEntry.Record(key.resourceKey(), key.soundType(), soundKey, volume, pitch);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
