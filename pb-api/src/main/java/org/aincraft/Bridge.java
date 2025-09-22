package org.aincraft;

import org.jetbrains.annotations.NotNull;

public interface Bridge {

  static Bridge bridge() throws IllegalStateException {
    return BridgeAccessor.bridgeAccess();
  }

  @NotNull
  EntityModelFactory getEntityModelFactory();

}
