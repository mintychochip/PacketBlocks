package org.aincraft;

import com.google.inject.Inject;
import org.aincraft.registry.RegistryAccess;
import org.jetbrains.annotations.NotNull;

record BridgeImpl(RegistryAccess registryAccess, ItemService itemService) implements Bridge {

  @Inject
  BridgeImpl {
  }

  @Override
  public @NotNull EntityModelFactory entityModelFactory() {
    return (type, location) -> EntityModelImpl.create(type, location.getWorld(),
        location.toVector());
  }
}
