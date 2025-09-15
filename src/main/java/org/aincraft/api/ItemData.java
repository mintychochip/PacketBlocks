package org.aincraft.api;

import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.aincraft.api.Builder.Buildable;
import org.aincraft.api.ItemData.ItemDataBuilder;
import org.aincraft.api.ItemDataImpl.BuilderImpl;
import org.bukkit.Material;

public interface ItemData extends Buildable<ItemDataBuilder, ItemData> {

  static ItemDataBuilder builder() {
    return new BuilderImpl();
  }

  Key itemModel();

  Material material();

  Component displayName();

  ItemLore lore();

  interface ItemDataBuilder extends org.aincraft.api.Builder<ItemData> {

    ItemDataBuilder itemModel(Key key);

    ItemDataBuilder material(Material material);

    ItemDataBuilder displayName(Component displayName);

    ItemDataBuilder lore(ItemLore lore);
  }
}
