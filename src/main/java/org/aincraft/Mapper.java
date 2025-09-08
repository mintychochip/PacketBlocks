package org.aincraft;

import org.jetbrains.annotations.NotNull;

public interface Mapper<D, R> {

  @NotNull
  D asDomain(@NotNull R record) throws IllegalArgumentException;

  @NotNull
  R asRecord(@NotNull D domain) throws IllegalArgumentException;
}
