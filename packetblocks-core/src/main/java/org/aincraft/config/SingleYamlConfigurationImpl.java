package org.aincraft.config;

import com.google.common.base.Preconditions;
import java.io.File;
import java.lang.reflect.Proxy;
import org.bukkit.plugin.Plugin;

final class SingleYamlConfigurationImpl {

  private final Plugin plugin;
  private final String path;
  private org.bukkit.configuration.file.YamlConfiguration config;
  private File configFile;

  SingleYamlConfigurationImpl(Plugin plugin, String path) {
    this.plugin = plugin;
    this.path = path;
    this.configFile = new File(plugin.getDataFolder(), path);
    if (!configFile.exists()) {
      plugin.saveResource(path, false);
    }
    assert (configFile != null);
    config = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(configFile);
  }

  static YamlConfiguration single(Plugin plugin, String path) throws IllegalArgumentException {
    String[] split = path.split("\\.");
    Preconditions.checkArgument(split.length >= 2);
    Preconditions.checkArgument(split[1].equals("yml") || split[1].equals("yaml"));
    SingleYamlConfigurationImpl configuration = new SingleYamlConfigurationImpl(plugin, path);
    return (YamlConfiguration) Proxy.newProxyInstance(YamlConfiguration.class.getClassLoader(),
        new Class[]{YamlConfiguration.class},
        (proxy, method, args) -> method.invoke(configuration.config, args));
  }
}
