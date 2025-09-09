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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ModelDataRepositoryImpl implements Repository.Writable<String, ModelData.Record> {

  private final ConnectionSource connectionSource;
  private final Cache<String, ModelData> readCache = Caffeine.newBuilder().expireAfterAccess(
      Duration.ofMinutes(10)).build();

  @Inject
  ModelDataRepositoryImpl(ConnectionSource connectionSource) {
    this.connectionSource = connectionSource;
  }

  @Override
  public boolean save(@NotNull ModelData.Record record) {
    final String sql =
        "INSERT INTO client_block_data (" +
            "resource_key, item_model, " +
            "tx, ty, tz, " +
            "lx, ly, lz, lw, " +
            "sx, sy, sz, " +
            "rx, ry, rz, rw, range, block, sky" +
            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setString(1, record.resourceKey());
      ps.setString(2, record.itemModel());

      ps.setFloat(3, record.tx());
      ps.setFloat(4, record.ty());
      ps.setFloat(5, record.tz());

      ps.setFloat(6, record.lx());
      ps.setFloat(7, record.ly());
      ps.setFloat(8, record.lz());
      ps.setFloat(9, record.lw());

      ps.setFloat(10, record.sx());
      ps.setFloat(11, record.sy());
      ps.setFloat(12, record.sz());

      ps.setFloat(13, record.rx());
      ps.setFloat(14, record.ry());
      ps.setFloat(15, record.rz());
      ps.setFloat(16, record.rw());

      ps.setFloat(17, record.range());
      ps.setInt(18, record.block());
      ps.setInt(19, record.sky());

      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace(); // log so you can see what went wrong
      return false;
    }
  }

  @Override
  public boolean delete(String key) {
    return false;
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
