package org.aincraft.domain;

import org.aincraft.api.PacketBlockData;
import org.jetbrains.annotations.Nullable;

public interface PacketBlockDataService {

  @Nullable
  PacketBlockData load(String resourceKey);
}
