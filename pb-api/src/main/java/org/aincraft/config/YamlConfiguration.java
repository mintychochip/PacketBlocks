package org.aincraft.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface YamlConfiguration extends FileBackedConfiguration, ConfigurationSection {

  @NotNull
  static YamlConfiguration create(Plugin plugin, String path) throws IllegalArgumentException {
    return YamlFileBackedConfigurationImpl.create(plugin,path);
  }
}
