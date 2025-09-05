package org.aincraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Metal extends JavaPlugin {

  @Override
  public void onEnable() {
    Bukkit.getPluginCommand("test").setExecutor(new TestCommand(this));
  }
}
