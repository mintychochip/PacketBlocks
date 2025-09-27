package org.aincraft.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public interface YamlConfiguration extends ConfigurationSection {

  static YamlConfiguration single(Plugin plugin, String path) {
    return SingleYamlConfigurationImpl.single(plugin, path);
  }
}
