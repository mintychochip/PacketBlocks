package org.aincraft.api;

public interface PacketBlock {

  BlockModel model();

  ModelData modelData();

  SpoofData spoofData();
}
