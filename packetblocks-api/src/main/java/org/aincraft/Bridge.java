package org.aincraft;

import org.aincraft.registry.RegistryAccess;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

@AvailableSince("1.0.2")
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
