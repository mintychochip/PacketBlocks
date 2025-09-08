package org.aincraft;

import java.util.function.Consumer;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.ClientBlock;
import org.aincraft.api.ClientBlock.Builder;
import org.aincraft.api.ClientBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public record BlockBindingImpl(ClientBlockData blockData, Location location) implements
    BlockBinding {

}
