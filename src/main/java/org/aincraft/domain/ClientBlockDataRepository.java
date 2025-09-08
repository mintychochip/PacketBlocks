package org.aincraft.domain;

import org.aincraft.api.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClientBlockDataRepository {

  boolean save(@NotNull ModelData.Record record);

  @Nullable
  ModelData.Record load(String resourceKey);
}
