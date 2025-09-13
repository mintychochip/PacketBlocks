package org.aincraft.api;

import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

record ItemDataImpl(Key itemModel, Material material, Component displayName,
                    ItemLore lore) implements ItemData {

  @Override
  public ItemDataBuilder toBuilder() {
    return new BuilderImpl()
        .itemModel(itemModel)
        .material(material);
  }

  static final class BuilderImpl implements ItemDataBuilder {

    private Key itemModel;
    private Material material;
    private Component displayName;
    private ItemLore lore;

    @Override
    public ItemDataBuilder itemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    @Override
    public ItemDataBuilder material(Material material) {
      this.material = material;
      return this;
    }

    @Override
    public ItemData build() {
      return new ItemDataImpl(itemModel, material, displayName, lore);
    }
  }
}
