package org.aincraft.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.api.PacketBlockData;
import org.aincraft.config.ConfigurationFactory;
import org.aincraft.config.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class PacketBlockDataRepositoryProviderImpl implements Provider<PacketBlockDataRepository> {

  private final ConfigurationFactory<PacketBlockData> packetBlockDataFactory;
  private final YamlConfiguration configuration;

  @Inject
  public PacketBlockDataRepositoryProviderImpl(
      ConfigurationFactory<PacketBlockData> packetBlockDataFactory,
      YamlConfiguration configuration) {
    this.packetBlockDataFactory = packetBlockDataFactory;
    this.configuration = configuration;
  }

  @Override
  public PacketBlockDataRepository get() {
    Map<String, PacketBlockData> packetBlockData = new HashMap<>();
    for (String resourceKey : configuration.getKeys(false)) {
      ConfigurationSection resourceSection = configuration.getConfigurationSection(
          resourceKey);
      if (resourceSection == null) {
        continue;
      }

      PacketBlockData blockData = packetBlockDataFactory.create(resourceSection);
      packetBlockData.put(resourceKey,blockData);
    }
    Bukkit.getLogger().info(packetBlockData.toString());
    return packetBlockData::get;
  }
}
