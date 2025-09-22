package org.aincraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

final class SQLiteSourceImpl implements ConnectionSource {

  private final Plugin plugin;
  private final Path databaseFilePath;

  private SQLiteSourceImpl(Plugin plugin, Path databaseFilePath) {
    this.plugin = plugin;
    this.databaseFilePath = databaseFilePath;
  }

  static SQLiteSourceImpl create(@NotNull Plugin plugin, String relativePath) {
    File dataFolder = plugin.getDataFolder();
    Path databaseFilePath = dataFolder.toPath().resolve(relativePath);

    File databaseFile = new File(databaseFilePath.toString());
    File parentFile = databaseFile.getParentFile();
    if (!parentFile.exists()) {
      parentFile.mkdirs();
    }
    if (!databaseFile.exists()) {
      try {
        if (!databaseFile.createNewFile()) {
          throw new IOException("failed to create database flat file");
        }
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    SQLiteSourceImpl source = new SQLiteSourceImpl(plugin, databaseFilePath);
    try (Connection connection = source.getConnection()) {
      connection.setAutoCommit(false);
      Savepoint savepoint = connection.setSavepoint();

      try (Statement stmt = connection.createStatement()) {
        for (String query : source.getSQLTables()) {
          stmt.addBatch(query);
        }
        stmt.executeBatch();
        connection.commit();
      } catch (SQLException e) {
        connection.rollback(savepoint);
        throw new SQLException("Error executing bulk SQL", e);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return source;
  }

  @NotNull
  private static String getUrl(@NotNull Path databaseFilePath) {
    return String.format("jdbc:sqlite:%s",
        databaseFilePath.toAbsolutePath());
  }

  public String[] getSQLTables() {
    try (InputStream resourceStream = ResourceExtractor.getResourceStream(
        "sqlite.sql");
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(resourceStream, StandardCharsets.UTF_8))) {
      Stream<String> lines = reader.lines();
      String tables = lines.collect(Collectors.joining("\n"));
      return Arrays.stream(tables.split(";"))
          .map(s -> s.trim() + ";")
          .filter(s -> !s.equals(";"))
          .toArray(String[]::new);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Connection getConnection() {
    try {
      Connection connection = DriverManager.getConnection(getUrl(databaseFilePath));
      try (Statement st = connection.createStatement()) {
        st.execute("PRAGMA foreign_keys=ON;");
        st.execute("PRAGMA synchronous=NORMAL;");
      }
      return connection;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to open SQLite connection", e);
    }
  }
}
