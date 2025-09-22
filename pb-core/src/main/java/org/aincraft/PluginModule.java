package org.aincraft;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import java.time.Duration;
import net.kyori.adventure.key.Key;
import org.aincraft.EntityModelData.EntityModelAttribute;
import org.aincraft.EntityModelImpl.EntityModelDataImpl;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.Registry;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PluginModule extends AbstractModule {

  private final Plugin plugin;

  public PluginModule(Plugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  Registry<PacketBlockMeta> blockMetaRegistry() {
    EntityModelData data = EntityModelDataImpl.create();
    data.setAttribute(EntityModelAttributes.ITEM_MODEL,Key.key("packetblocks:electrum_ore"));
    Registry<PacketBlockMeta> simple = Registry.createSimple();
    simple.register(new PacketBlockMetaImpl(Key.key("packetblocks:electrum_ore"),
        new BlockItemMeta() {
          @Override
          public Key getItemModel() {
            return Key.key("packetblocks:electrum_ore");
          }

          @Override
          public Material getMaterial() {
            return Material.STONE;
          }
        }, data));
    return simple;
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
    bind(ConnectionSource.class).toInstance(SQLiteSourceImpl.create(plugin,"block.db"));
    bind(ItemService.class).to(ItemServiceImpl.class).in(Singleton.class);
    bind(BlockModelService.class).to(BlockModelServiceImpl.class).in(Singleton.class);
    bind(PacketBlockService.class).to(PacketBlockServiceImpl.class).in(Singleton.class);
    Multibinder<Listener> binder = Multibinder.newSetBinder(binder(), Listener.class);
    binder.addBinding().to(BlockController.class);
  }
}
