package org.aincraft.domain.yaml;

import java.util.Locale;
import org.aincraft.api.ItemData;
import org.aincraft.api.ItemData.ItemDataBuilder;
import org.aincraft.config.ConfigurationFactory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ItemDataFactoryImpl implements ConfigurationFactory<ItemData> {

  private static final ItemData DEFAULT = ItemData.builder()
      .itemModel(Material.STONE.key())
      .material(Material.STONE)
      .build();

  @Override
  public @NotNull ItemData create(@Nullable ConfigurationSection section)
      throws IllegalArgumentException {
    if (section == null) {
      return DEFAULT;
    }
    ItemDataBuilder builder = DEFAULT.toBuilder();
    if (section.contains("item-model")) {
      builder.itemModel(NamespacedKey.fromString(section.getString("item-model")));
    }
    if (section.contains("material")) {
      builder.material(Material.valueOf(section.getString("material").toUpperCase(Locale.ENGLISH)));
    }
    if (section.contains("display-name")) {
      builder.displayName(section.getRichMessage("display-name"));
    }
    return builder.build();
  }
}
