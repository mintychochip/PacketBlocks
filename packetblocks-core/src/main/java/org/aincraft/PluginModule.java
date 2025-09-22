package org.aincraft;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import java.time.Duration;
import java.util.logging.Logger;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.Registry;
import org.aincraft.registry.RegistryAccess;
import org.aincraft.registry.RegistryAccessKeys;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PluginModule extends AbstractModule {

  private final Plugin plugin;

  public PluginModule(Plugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  Registry<PacketBlockMeta> blockMetaRegistry(RegistryAccess registryAccess) {
    return registryAccess.getRegistry(RegistryAccessKeys.PACKET_BLOCK_META);
  }

  @Provides
  @Singleton
  Registry<KeyedItem> itemRegistry(RegistryAccess registryAccess) {
    return registryAccess.getRegistry(RegistryAccessKeys.ITEM);
  }

  @Provides
  @Singleton
  BlockBindingRepository blockBindingRepository(Plugin plugin, ConnectionSource connectionSource) {
    RelationalBlockBindingRepositoryImpl blockBindingRepository = new RelationalBlockBindingRepositoryImpl(
        connectionSource);
    return WriteBackBlockBindingRepositoryImpl.create(plugin, 50, Duration.ofSeconds(10),
        blockBindingRepository);
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(ConnectionSource.class).toInstance(SQLiteSourceImpl.create(plugin, "block.db"));
    bind(ItemService.class).to(ItemServiceImpl.class).in(Singleton.class);
    bind(BlockModelService.class).to(BlockModelServiceImpl.class).in(Singleton.class);
    bind(PacketBlockService.class).to(PacketBlockServiceImpl.class).in(Singleton.class);
    bind(RegistryAccess.class).to(RegistryAccessImpl.class).in(Singleton.class);
    Multibinder<Listener> listenerBinder = Multibinder.newSetBinder(binder(), Listener.class);
    listenerBinder.addBinding().to(BlockController.class);
  }
}
