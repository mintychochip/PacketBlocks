package org.aincraft.domain;

import org.aincraft.api.BlockBinding;
import org.bukkit.Location;

public interface BlockBindingFactory {

  BlockBinding bind(Location location, String resourceKey);
}
