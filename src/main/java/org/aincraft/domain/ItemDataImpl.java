package org.aincraft.domain;

import net.kyori.adventure.key.Key;
import org.aincraft.api.ItemData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record ItemDataImpl(Key resourceKey, Key itemModel, Material material) implements ItemData {

}
