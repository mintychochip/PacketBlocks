package org.aincraft;

import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Metal extends JavaPlugin {

  @Override
  public void onEnable() {
    Injector injector = Guice.createInjector(new PluginModule(this));
    Set<Listener> listeners = injector.getInstance(com.google.inject.Key.get(new TypeLiteral<>(){}));
    listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener,this));
    Bukkit.getPluginCommand("item").setExecutor(injector.getInstance(ItemCommand.class));
    Bukkit.getPluginCommand("test").setExecutor(injector.getInstance(TestCommand.class));
  }

  @Override
  public void onDisable() {
    super.onDisable();
  }
}
