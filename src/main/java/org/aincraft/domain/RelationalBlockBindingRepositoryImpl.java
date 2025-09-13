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

  private final ConnectionSource connectionSource;

  @Inject
  public RelationalBlockBindingRepositoryImpl(
      ConnectionSource connectionSource) {
    this.connectionSource = connectionSource;
  }

  @Override
  public boolean save(BlockBinding blockBinding) {
    String sql = "INSERT INTO block_bindings (world, x, y, z, cx, cz, resource_key) VALUES (?,?,?,?,?,?,?)";
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setString(1, blockBinding.worldName());
      ps.setDouble(2, blockBinding.x());
      ps.setDouble(3, blockBinding.y());
      ps.setDouble(4, blockBinding.z());
      ps.setInt(5, blockBinding.chunkX());
      ps.setInt(6, blockBinding.chunkZ());
      ps.setString(7, blockBinding.resourceKey());

      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to save BlockBinding", e);
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
  public BlockBinding load(Location location) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT resource_key FROM block_bindings WHERE world=? AND x=? AND y=? AND z=?")) {
      ps.setString(1, location.getWorld().getName());
      ps.setInt(2, location.getBlockX());
      ps.setInt(3, location.getBlockY());
      ps.setInt(4, location.getBlockZ());
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          return null;
        }
        String resourceKey = rs.getString("resource_key");
        return BlockBinding.create(location,resourceKey);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<BlockBinding> loadAllByChunk(Chunk chunk) {
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT world, x, y, z, resource_key FROM block_bindings WHERE cx=? AND cz=?")) {
      ps.setInt(1, chunk.getX());
      ps.setInt(2, chunk.getZ());
      List<BlockBinding> bindings = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          String world = rs.getString("world");
          double x = rs.getDouble("x");
          double y = rs.getDouble("y");
          double z = rs.getDouble("z");
          String resourceKey = rs.getString("resource_key");
          bindings.add(BlockBinding.create(world, x, y, z, resourceKey));
        }
      }
      return bindings;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
