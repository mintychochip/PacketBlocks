package org.aincraft.api;

public interface PacketBlock {

  BlockModel blockModel();

  ModelData modelData();

  SoundData soundData();

  ItemData itemData();
}
