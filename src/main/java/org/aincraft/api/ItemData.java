package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface ItemData {

  Key resourceKey();

  Key itemModel();

  Material material();
}
