package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.aincraft.domain.Repository;

public interface PacketBlockData {

  Key resourceKey();

  ModelData modelData();

  ItemData itemData();

  SoundData soundData();

  record Record(String resourceKey, ModelDataRecord modelDataRecord,
                SoundData.Record soundDataRecord, ItemDataRecord itemDataRecord) implements
      Repository

          .Record<String> {

    @Override
    public String key() {
      return resourceKey;
    }
  }
}
