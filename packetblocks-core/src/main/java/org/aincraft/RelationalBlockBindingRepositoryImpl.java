package org.aincraft;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

final class RelationalBlockBindingRepositoryImpl implements BlockBindingRepository {

  private static final Duration TTL = Duration.ofMinutes(10);
  private final ConnectionSource connectionSource;

  private final Cache<LocationKey, BlockBinding> readCache = Caffeine.newBuilder()
      .expireAfterAccess(TTL).build();

  @Inject
  public RelationalBlockBindingRepositoryImpl(
      ConnectionSource connectionSource) {
    this.connectionSource = connectionSource;
  }

  @Override
  public boolean save(BlockBinding binding) {
    String sql = "INSERT INTO block_bindings (world, x, y, z, cx, cz, resource_key) VALUES (?,?,?,?,?,?,?)";
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, binding.worldName());
      ps.setDouble(2, binding.x());
      ps.setDouble(3, binding.y());
      ps.setDouble(4, binding.z());
      ps.setInt(5, binding.chunkX());
      ps.setInt(6, binding.chunkZ());
      ps.setString(7, binding.resourceKey());
      readCache.put(LocationKey.create(binding.location()), binding);
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
      ps.setDouble(2, location.x());
      ps.setDouble(3, location.y());
      ps.setDouble(4, location.z());
      readCache.invalidate(LocationKey.create(location));
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public BlockBinding load(Location location) {
    LocationKey locationKey = LocationKey.create(location);
    BlockBinding binding = readCache.getIfPresent(locationKey);
    if (binding != null) {
      return binding;
    }
    try (Connection connection = connectionSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
            "SELECT resource_key FROM block_bindings WHERE world=? AND x=? AND y=? AND z=?")) {
      ps.setString(1, location.getWorld().getName());
      ps.setDouble(2, location.x());
      ps.setDouble(3, location.y());
      ps.setDouble(4, location.z());
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          return null;
        }
        String resourceKey = rs.getString("resource_key");
        binding = new BlockBindingImpl(location, resourceKey);
        readCache.put(locationKey,binding);
        return binding;
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
          BlockBinding binding = readCache.getIfPresent(
              new LocationKey(world, x, y, z, chunk.getX(), chunk.getZ()));
          if (binding != null) {
            bindings.add(binding);
            continue;
          }
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
