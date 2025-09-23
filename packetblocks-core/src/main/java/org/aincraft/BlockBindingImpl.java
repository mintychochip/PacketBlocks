package org.aincraft;

import org.bukkit.Location;

public record BlockBindingImpl(Location location, String resourceKey) implements BlockBinding {

}
