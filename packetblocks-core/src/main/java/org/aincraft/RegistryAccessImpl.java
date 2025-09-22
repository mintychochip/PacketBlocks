package org.aincraft;

import net.kyori.adventure.key.Keyed;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.Registry;
import org.aincraft.registry.RegistryAccess;
import org.jetbrains.annotations.NotNull;

final class RegistryAccessImpl implements RegistryAccess {

  private final Registry<PacketBlockMeta> META_REGISTRY = Registry.createSimple();
  private final Registry<KeyedItem> ITEM_REGISTRY = Registry.createSimple();

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull <T extends Keyed> Registry<T> getRegistry(RegistryAccessKey<T> registryAccessKey)
      throws IllegalStateException {
    return switch (registryAccessKey.getKey()) {
      case "packet_block_meta" -> (Registry<T>) META_REGISTRY;
      case "keyed_item" -> (Registry<T>) ITEM_REGISTRY;
      default -> throw new IllegalStateException("Unexpected value: " + registryAccessKey.getKey());
    };
  }
}
