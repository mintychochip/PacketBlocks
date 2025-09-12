package org.aincraft.domain;

import org.aincraft.api.PacketBlockData;
import org.jetbrains.annotations.Nullable;

public interface PacketBlockDataRepository {

  @Nullable
  PacketBlockData.Record load(String resourceKey);
}
