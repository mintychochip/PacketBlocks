package org.aincraft.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.aincraft.Bridge;
import org.aincraft.KeyedItem;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

@AvailableSince("1.0.2")
public interface RegistryAccess {

  @NotNull
  @AvailableSince("1.0.2")
  static RegistryAccess registryAccess() {
    return Bridge.bridge().registryAccess();
  }

  @NotNull
  <T extends Keyed> Registry<T> getRegistry(RegistryAccessKey<T> registryAccessKey)
      throws IllegalStateException;

  interface RegistryAccessKey<T extends Keyed> {

    String getKey();
  }
}
