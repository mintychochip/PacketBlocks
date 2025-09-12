package org.aincraft;

import net.kyori.adventure.key.Key;
import org.aincraft.api.BlockBinding;
import org.bukkit.Location;

public record BlockBindingImpl(Key resourceKey, ModelData blockData, Location location) implements
    BlockBinding {

}
