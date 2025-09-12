package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.api.Builder.Buildable;
import org.bukkit.Material;

public interface ItemData extends Buildable<ItemData.Builder, ItemData> {

  ItemData DEFAULT = ItemDataImpl.DEFAULT;

  Key itemModel();

  Material material();

  interface Builder extends org.aincraft.api.Builder<ItemData> {

    Builder itemModel(Key key);

    Material material(Material material);
  }
}
