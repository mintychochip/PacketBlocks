package org.aincraft.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import net.kyori.adventure.key.Key;
import org.aincraft.ConnectionSource;
import org.aincraft.Mapper;
import org.aincraft.PacketItemService;
import org.aincraft.PacketItemServiceImpl;
import org.aincraft.adapter.ClientBlockDataFactoryImpl;
import org.aincraft.adapter.KyoriKeyAdapterImpl;
import org.aincraft.api.BlockBinding;
import org.aincraft.config.YamlConfiguration;
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
    Gson gson = new GsonBuilder().registerTypeAdapter(Key.class, new KyoriKeyAdapterImpl())
        .registerTypeAdapterFactory(new ClientBlockDataFactoryImpl()).create();
    return new PacketItemServiceImpl(itemKey, gson);
  }

  private static final class RepositoryModule extends PrivateModule {

    @Override
    protected void configure() {
      bind(new TypeLiteral<Mapper<ModelData, ModelData.Record>>() {
      })
          .to(ModelDataMapperImpl.class)
          .in(Singleton.class);
      bind(new TypeLiteral<Mapper<BlockBinding, BlockBinding.Record>>() {
      })
          .to(BlockBindingMapperImpl.class)
          .in(Singleton.class);
      bind(new TypeLiteral<Mapper<SoundEntry, SoundEntry.Record>>() {
      })
          .to(SoundEntryMapperImpl.class)
          .in(Singleton.class);
      bind(new TypeLiteral<Mapper<SoundData, SoundData.Record>>() {
      })
          .to(SoundDataMapperImpl.class)
          .in(Singleton.class);
      bind(new TypeLiteral<Repository<String, ModelData.Record>>() {
      })
          .toProvider(ModelDataRepositoryProviderImpl.class)
          .in(Singleton.class);
      bind(new TypeLiteral<Repository<String, SoundData.Record>>() {
      })
          .toProvider(SoundDataRepositoryProviderImpl.class)
          .in(Singleton.class);
      bind(Service.class).to(ServiceImpl.class).in(Singleton.class);
      expose(Service.class);
      bind(BlockModelService.class).to(BlockModelServiceImpl.class).in(Singleton.class);
      expose(BlockModelService.class);
      bind(PacketBlockService.class).to(PacketBlockServiceImpl.class).in(Singleton.class);
      expose(PacketBlockService.class);
      bind(BlockBindingFactory.class).to(BlockBindingFactoryImpl.class).in(Singleton.class);
      expose(BlockBindingFactory.class);
    }

    @Provides
    YamlConfiguration configuration(Plugin plugin) {
      return YamlConfiguration.create(plugin, "config.yml");
    }

    @Provides
    @Singleton
    BlockBindingRepository blockBindingRepository(
        Repository<String, ModelData.Record> blockDataRepository,
        ConnectionSource connectionSource, Plugin plugin) {
      RelationalBlockBindingRepositoryImpl repository = new RelationalBlockBindingRepositoryImpl(
          blockDataRepository, connectionSource);
      return WriteBackBlockBindingRepositoryImpl.create(repository, plugin);
    }
  }
}
