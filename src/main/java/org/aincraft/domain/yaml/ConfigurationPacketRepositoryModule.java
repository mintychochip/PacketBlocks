package org.aincraft.domain.yaml;

import com.google.inject.AbstractModule;
import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import org.aincraft.api.ItemData;
import org.aincraft.api.ModelData;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.SoundData;
import org.aincraft.config.ConfigurationFactory;

public final class ConfigurationPacketRepositoryModule extends PrivateModule {

  @Override
  protected void configure() {
    bind(new TypeLiteral<ConfigurationFactory<ModelData>>() {
    })
        .to(ModelDataFactoryImpl.class)
        .in(Singleton.class);
    bind(new TypeLiteral<ConfigurationFactory<ItemData>>() {
    })
        .to(ItemDataFactoryImpl.class)
        .in(Singleton.class);
    bind(new TypeLiteral<ConfigurationFactory<SoundData>>() {
    })
        .to(SoundDataFactoryImpl.class)
        .in(Singleton.class);
    bind(new TypeLiteral<ConfigurationFactory<PacketBlockData>>() {
    })
        .to(PacketBlockDataFactoryImpl.class)
        .in(Singleton.class);
    expose(new TypeLiteral<ConfigurationFactory<PacketBlockData>>() {
    });
  }
}
