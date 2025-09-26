package org.aincraft;

import java.util.Map;
import net.kyori.adventure.key.Key;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.bukkit.Location;

public interface PacketBlockFactory {

  BlockModel create(Location location);

  BlockModelData.Builder dataBuilder();

  PacketBlockMeta createBlockMeta(Key key, BlockItemMeta blockItemMeta,
      BlockModelData blockModelData,
      Map<SoundType, SoundEntry> entries);
}
