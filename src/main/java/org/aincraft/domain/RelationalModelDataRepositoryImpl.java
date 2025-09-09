package org.aincraft.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import org.aincraft.ConnectionSource;
import org.aincraft.api.ModelData;
import org.jetbrains.annotations.Nullable;

final class RelationalModelDataRepositoryImpl implements Repository<String, ModelData.Record> {

  private final ConnectionSource connectionSource;

  @Inject
  RelationalModelDataRepositoryImpl(ConnectionSource connectionSource) {
    this.connectionSource = connectionSource;
  }

  @Override
  public @Nullable ModelData.Record load(String resourceKey) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT item_model, tx, ty, tz, lx, ly, lz, lw, sx, sy, sz, rx, ry, rz, rw, range, block, sky FROM client_block_data WHERE resource_key = ?")) {
      ps.setString(1, resourceKey);
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          return null;
        }
        String itemModel = rs.getString("item_model");
        int block = rs.getInt("block");
        int sky = rs.getInt("sky");

        float tx = rs.getFloat("tx");
        float ty = rs.getFloat("ty");
        float tz = rs.getFloat("tz");

        float lx = rs.getFloat("lx");
        float ly = rs.getFloat("ly");
        float lz = rs.getFloat("lz");
        float lw = rs.getFloat("lw");

        float sx = rs.getFloat("sx");
        float sy = rs.getFloat("sy");
        float sz = rs.getFloat("sz");

        float rx = rs.getFloat("rx");
        float ry = rs.getFloat("ry");
        float rz = rs.getFloat("rz");
        float rw = rs.getFloat("rw");
        float range = rs.getFloat("range");
        return new ModelData.Record(resourceKey, itemModel,
            tx, ty, tz,
            lx, ly, lz, lw,
            sx, sy, sz,
            rx, ry, rz, rw, range, block, sky);

      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
