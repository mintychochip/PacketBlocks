package org.aincraft.domain;

import java.util.Locale;
import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.ItemData;
import org.aincraft.api.ItemDataRecord;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

final class ItemDataMapperImpl implements Mapper<ItemData, ItemDataRecord> {

  @Override
  public @NotNull ItemData asDomain(@NotNull ItemDataRecord record) throws IllegalArgumentException {
    Key itemModel = NamespacedKey.fromString(record.itemModel());
    Material material = Material.valueOf(record.material().toUpperCase(Locale.ENGLISH));
    return new ItemData() {
      @Override
      public Key itemModel() {
        return itemModel;
      }

      @Override
      public Material material() {
        return material;
      }
    };
  }

  @Override
  public @NotNull ItemDataRecord asRecord(@NotNull ItemData domain) throws IllegalArgumentException {
    String itemModel = domain.itemModel().toString();
    String material = domain.material().toString().toLowerCase(Locale.ENGLISH);
    return new ItemDataRecord(itemModel, material);
  }
}
