package org.aincraft;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;

public interface BlockItemMeta {

  Key getItemModel();

  Material getMaterial();
}
