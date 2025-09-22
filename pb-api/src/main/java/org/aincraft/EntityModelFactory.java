package org.aincraft;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface EntityModelFactory {

  EntityModel create(EntityType entityType, Location location);
}
