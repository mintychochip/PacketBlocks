package org.aincraft.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import java.time.Duration;
import net.kyori.adventure.key.Key;
import org.aincraft.ConnectionSource;
import org.aincraft.Mapper;
import org.aincraft.PacketItemService;
import org.aincraft.PacketItemServiceImpl;
import org.aincraft.config.YamlConfiguration;
import org.aincraft.domain.yaml.ConfigurationPacketRepositoryModule;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public final class ServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new RepositoryModule());
    Multibinder<Listener> binder = Multibinder.newSetBinder(binder(), Listener.class);
    binder.addBinding().to(BlockController.class);
  }

  @Provides
  @Singleton
  public PacketItemService packetItemService(Plugin plugin) {
    NamespacedKey itemKey = new NamespacedKey(plugin, "items");
    Gson gson = new GsonBuilder().create();
    return new PacketItemServiceImpl(itemKey, gson);
  }

  private static final class RepositoryModule extends PrivateModule {

    @Override
    protected void configure() {
      install(new ConfigurationPacketRepositoryModule());
      bind(PacketBlockDataRepository.class).toProvider(PacketBlockDataRepositoryProviderImpl.class)
          .in(Singleton.class);
      bind(Service.class).to(ServiceImpl.class).in(Singleton.class);
      expose(Service.class);
      bind(BlockModelService.class).to(BlockModelServiceImpl.class).in(Singleton.class);
      expose(BlockModelService.class);
      bind(PacketBlockService.class).to(PacketBlockServiceImpl.class).in(Singleton.class);
      expose(PacketBlockService.class);
    }

    @Provides
    YamlConfiguration configuration(Plugin plugin) {
      return YamlConfiguration.create(plugin, "config.yml");
    }

    @Provides
    @Singleton
    @Exposed
    BlockBindingRepository blockBindingRepository(
        ConnectionSource connectionSource, Plugin plugin) {
      RelationalBlockBindingRepositoryImpl repository = new RelationalBlockBindingRepositoryImpl(
          connectionSource);
      return WriteBackBlockBindingRepositoryImpl.create(plugin, 50, Duration.ofSeconds(10),
          repository);
    }
  }
}
