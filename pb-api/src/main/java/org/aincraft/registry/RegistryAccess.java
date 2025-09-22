package org.aincraft.registry;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.aincraft.Bridge;
import org.aincraft.KeyedItem;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.jetbrains.annotations.NotNull;

public interface RegistryAccess {

  @NotNull
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
