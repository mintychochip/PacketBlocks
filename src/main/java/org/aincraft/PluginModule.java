package org.aincraft;

import com.google.inject.AbstractModule;
import org.aincraft.domain.ServiceModule;
import org.bukkit.plugin.Plugin;

public final class PluginModule extends AbstractModule {

  private final Plugin plugin;

  public PluginModule(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(ConnectionSource.class).toInstance(SQLiteSourceImpl.create(plugin,"metal.db"));
    bind(Plugin.class).toInstance(plugin);
    install(new ServiceModule());
  }
}
