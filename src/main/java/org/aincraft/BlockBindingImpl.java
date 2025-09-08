package org.aincraft;

import org.aincraft.api.BlockBinding;
import org.aincraft.api.ModelData;
import org.bukkit.Location;

public record BlockBindingImpl(ModelData blockData, Location location) implements
    BlockBinding {

}
