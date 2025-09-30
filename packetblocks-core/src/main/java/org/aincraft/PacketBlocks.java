package org.aincraft;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.Set;
import org.aincraft.commands.RootCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class PacketBlocks extends JavaPlugin {

  @Override
  public void onEnable() {
    Injector injector = Guice.createInjector(new PluginModule(this));
    try {
      BridgeImpl bridge = injector.getInstance(BridgeImpl.class);
      Bukkit.getServicesManager().register(Bridge.class, bridge, this, ServicePriority.High);
    } catch (ConfigurationException | ProvisionException ex) {
      Bukkit.getLogger().info("failed to init bridge");
    }
    this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
      RootCommand root = injector.getInstance(RootCommand.class);
      commands.registrar().register(root.build().build());
    });
    Set<Listener> listeners = injector.getInstance(com.google.inject.Key.get(new TypeLiteral<>() {
    }));
    listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    Bukkit.getPluginCommand("item").setExecutor(injector.getInstance(ItemCommand.class));

  }
}
