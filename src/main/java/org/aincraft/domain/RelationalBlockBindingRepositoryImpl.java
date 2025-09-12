package org.aincraft.domain;

import com.google.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.aincraft.ConnectionSource;
import org.aincraft.api.BlockBinding;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

final class RelationalBlockBindingRepositoryImpl implements BlockBindingRepository {

  private final Repository<String, ModelData.Record> modelDataRepository;
  private final ConnectionSource connectionSource;

  @Inject
  public RelationalBlockBindingRepositoryImpl(
      Repository<String, ModelData.Record> modelDataRepository, ConnectionSource connectionSource) {
    this.modelDataRepository = modelDataRepository;
    this.connectionSource = connectionSource;
  }

  @Override
  public boolean save(@NotNull BlockBinding.Record record) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO block_bindings (world, x, y, z, cx, cz, resource_key) VALUES (?,?,?,?,?,?,?)")) {
      ps.setString(1, record.world());
      ps.setDouble(2, record.x());
      ps.setDouble(3, record.y());
      ps.setDouble(4, record.z());
      ps.setInt(5, record.cx());
      ps.setInt(6, record.cz());
      ps.setString(7, record.blockData().resourceKey());
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean delete(@NotNull Location location) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "DELETE FROM block_bindings WHERE world=? AND x=? AND y=? AND z=?")) {
      World world = location.getWorld();
      ps.setString(1, world.getName());
      ps.setInt(2, location.getBlockX());
      ps.setInt(3, location.getBlockY());
      ps.setInt(4, location.getBlockZ());
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public BlockBinding.Record load(Location location) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT cx,cz,resource_key FROM block_bindings WHERE world=? AND x=? AND y=? AND z=?")) {
      World world = location.getWorld();
      ps.setString(1, world.getName());
      ps.setInt(2, location.getBlockX());
      ps.setInt(3, location.getBlockY());
      ps.setInt(4, location.getBlockZ());
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          return null;
        }
        String resourceKey = rs.getString("resource_key");
        ModelData.Record record = modelDataRepository.load(resourceKey);
        if (record == null) {
          return null;
        }
        int cx = rs.getInt("cx");
        int cz = rs.getInt("cz");
        return new BlockBinding.Record(world.getName(), location.getBlockX(), location.getBlockY(),
            location.getBlockZ(), cx, cz, resourceKey, record);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<BlockBinding.Record> loadAllByChunk(Chunk chunk) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT world, x, y, z, resource_key FROM block_bindings WHERE cx=? AND cz=?")) {
      ps.setInt(1, chunk.getX());
      ps.setInt(2, chunk.getZ());
      List<BlockBinding.Record> bindings = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String world = rs.getString("world");
          double x = rs.getDouble("x");
          double y = rs.getDouble("y");
          double z = rs.getDouble("z");
          String resourceKey = rs.getString("resource_key");
          ModelData.Record record = modelDataRepository.load(resourceKey);
          if (record == null) {
            continue;
          }
          BlockBinding.Record binding = new BlockBinding.Record(world, x, y, z, chunk.getX(),
              chunk.getZ(), resourceKey, record);
          bindings.add(binding);
        }
      }
      return bindings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
