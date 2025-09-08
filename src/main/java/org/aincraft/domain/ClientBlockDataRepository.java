package org.aincraft.domain;

import java.util.UUID;
import net.kyori.adventure.key.Key;
import org.aincraft.api.ClientBlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClientBlockDataRepository {

  boolean save(@NotNull ClientBlockData.Record record);

  @Nullable
  ClientBlockData.Record load(String resourceKey);
}
