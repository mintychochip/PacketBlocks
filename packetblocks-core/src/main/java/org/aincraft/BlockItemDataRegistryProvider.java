package org.aincraft;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.aincraft.BlockItemMetaImpl.BuilderImpl;
import org.aincraft.config.YamlConfiguration;
import org.aincraft.registry.Registry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

final class BlockItemDataRegistryProvider implements Provider<Registry<BlockItemMeta>> {

  private static final String DEFAULT_ITEM_MODEL = "minecraft:stone";

  private final Plugin plugin;

  @Inject
  public BlockItemDataRegistryProvider(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public Registry<BlockItemMeta> get() {
    Registry<BlockItemMeta> registry = Registry.simple();
    YamlConfiguration configuration = YamlConfiguration.single(plugin, "config.yml");
    for (String key : configuration.getKeys(false)) {
      ConfigurationSection metaSection = configuration.getConfigurationSection(key);
      if (metaSection == null) {
        continue;
      }
      BuilderImpl builder = new BuilderImpl(NamespacedKey.fromString(key));
      if (metaSection.contains("display-name")) {
        builder.displayName(metaSection.getRichMessage("display-name"));
      }
      if (metaSection.contains("item-lore")) {
        List<Component> itemLore = metaSection.getStringList("item-lore").stream()
            .map(loreString -> MiniMessage.miniMessage().deserialize(loreString)).toList();
        builder.itemLore(itemLore);
      }
      if (metaSection.contains("item-model")) {
        builder.itemModel(NamespacedKey.fromString(metaSection.getString("item-model", DEFAULT_ITEM_MODEL)));
      }
      builder.material(Material.STONE);
      registry.register(builder.build());
    }
    return registry;
  }
}
