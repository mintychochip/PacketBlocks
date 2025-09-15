package org.aincraft.domain;

import java.util.List;
import net.kyori.adventure.key.Key;
import org.aincraft.api.ItemData;
import org.aincraft.api.ModelData;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.SoundData;
import org.aincraft.loot.LootData;

public record PacketBlockDataImpl(Key resourceKey, ModelData modelData, ItemData itemData,
                                  SoundData soundData, LootData lootData,
                                  List<String> tags) implements
    PacketBlockData {

}
