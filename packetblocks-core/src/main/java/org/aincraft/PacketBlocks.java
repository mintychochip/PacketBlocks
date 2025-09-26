package org.aincraft;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import java.util.HashMap;
import java.util.Set;
import net.kyori.adventure.key.Key;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.Registry;
import org.aincraft.registry.RegistryAccess;
import org.aincraft.registry.RegistryAccessKeys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    Set<Listener> listeners = injector.getInstance(com.google.inject.Key.get(new TypeLiteral<>() {
    }));
    listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    Bukkit.getPluginCommand("item").setExecutor(injector.getInstance(ItemCommand.class));
    Registry<PacketBlockMeta> metaRegistry = RegistryAccess.registryAccess()
        .getRegistry(RegistryAccessKeys.PACKET_BLOCK_META);
    BlockModelData data = BlockModelData.builder()
        .itemModel(Key.key("longhardfish:block/gutting_station")).build();
    metaRegistry.register(Bridge.bridge().packetBlockFactory()
        .createBlockMeta(Key.key("longhardfish:block/gutting_station"), new BlockItemMeta() {
          @Override
          public Key getItemModel() {
            return Key.key("penis");
          }

          @Override
          public Material getMaterial() {
            return Material.STONE;
          }
        }, data, new HashMap<>()));
    BlockModelData oreData = BlockModelData.builder()
        .itemModel( Key.key("packetblocks:electrum_ore")).build();
    metaRegistry.register(Bridge.bridge().packetBlockFactory()
        .createBlockMeta(Key.key("packetblocks:electrum_ore"), new BlockItemMeta() {
          @Override
          public Key getItemModel() {
            return Key.key("packetblocks:electrum_ore");
          }

          @Override
          public Material getMaterial() {
            return Material.STONE;
          }
        }, oreData, new HashMap<>()));
  }
}
