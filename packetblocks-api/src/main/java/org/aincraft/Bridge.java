package org.aincraft;

import org.aincraft.registry.RegistryAccess;
import org.jetbrains.annotations.NotNull;

public interface Bridge {

  static Bridge bridge() throws IllegalStateException {
    return BridgeAccessor.bridgeAccess();
  }

  @NotNull
  ItemService itemService();

  @NotNull
  PacketBlockFactory packetBlockFactory();

  @NotNull
  RegistryAccess registryAccess();
}
