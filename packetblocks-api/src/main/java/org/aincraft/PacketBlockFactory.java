package org.aincraft;

import java.util.Map;
import net.kyori.adventure.key.Key;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public interface PacketBlockFactory {

  EntityModel create(EntityType entityType, Location location);

  EntityModelData create();

  PacketBlockMeta createBlockMeta(Key key, BlockItemMeta blockItemMeta, EntityModelData entityModelData,
      Map<SoundType, SoundEntry> entries);
}
