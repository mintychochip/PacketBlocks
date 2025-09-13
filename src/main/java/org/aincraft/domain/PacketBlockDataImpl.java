package org.aincraft.domain;

import net.kyori.adventure.key.Key;
import org.aincraft.api.ItemData;
import org.aincraft.api.ModelData;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.SoundData;

public record PacketBlockDataImpl(Key resourceKey, ModelData modelData, ItemData itemData,
                                  SoundData soundData) implements PacketBlockData {

}
