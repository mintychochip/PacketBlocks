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
import org.aincraft.api.ModelData;
import org.aincraft.api.SoundEntry;
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
          .to(ClientBlockDataMapperImpl.class)
          .in(Singleton.class);
      bind(new TypeLiteral<Mapper<BlockBinding, BlockBinding.Record>>() {
      })
          .to(BlockBindingMapperImpl.class)
          .in(Singleton.class);
      bind(new TypeLiteral<Mapper<SoundEntry, SoundEntry.Record>>() {
      })
          .to(SoundEntryMapperImpl.class)
          .in(Singleton.class);
      bind(Service.class).to(ServiceImpl.class).in(Singleton.class);
      expose(Service.class);
      bind(BlockModelService.class).to(BlockModelServiceImpl.class).in(Singleton.class);
      expose(BlockModelService.class);
      bind(BlockBindingService.class).to(BlockBindingServiceImpl.class).in(Singleton.class);
      expose(BlockBindingService.class);
      bind(PacketBlockService.class).to(PacketBlockServiceImpl.class).in(Singleton.class);
      expose(PacketBlockService.class);
    }

    @Provides
    @Singleton
    Repository<String, ModelData.Record> modelDataRepository(
        ConnectionSource connectionSource) {
      return new RelationalModelDataRepositoryImpl(connectionSource);
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
