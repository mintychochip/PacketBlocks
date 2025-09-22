package org.aincraft;

import org.jetbrains.annotations.NotNull;

final class BridgeImpl implements Bridge {

  @Override
  public @NotNull EntityModelFactory getEntityModelFactory() {
    return (type, location) -> EntityModelImpl.create(type, location.getWorld(), location.toVector());
  }
}
