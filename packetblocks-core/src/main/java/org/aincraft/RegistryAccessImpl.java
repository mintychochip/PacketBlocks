package org.aincraft;

import com.google.inject.Inject;
import net.kyori.adventure.key.Keyed;
import org.aincraft.registry.Registry;
import org.aincraft.registry.RegistryAccess;
import org.jetbrains.annotations.NotNull;

final class RegistryAccessImpl implements RegistryAccess {

  private final Registry<KeyedItem> ITEM_REGISTRY = Registry.simple();
  private final Registry<BlockItemMeta> blockItemMetaRegistry;
  private final Registry<BlockModelData> blockModelDataRegistry;

  @Inject
  RegistryAccessImpl(Registry<BlockItemMeta> blockItemMetaRegistry,
      Registry<BlockModelData> blockModelDataRegistry) {
    this.blockItemMetaRegistry = blockItemMetaRegistry;
    this.blockModelDataRegistry = blockModelDataRegistry;
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull <T extends Keyed> Registry<T> getRegistry(RegistryAccessKey<T> registryAccessKey)
      throws IllegalStateException {
    return switch (registryAccessKey.getKey()) {
      case "block_item_meta" -> (Registry<T>) blockItemMetaRegistry;
      case "block_model_data" -> (Registry<T>) blockModelDataRegistry;
      case "keyed_item" -> (Registry<T>) ITEM_REGISTRY;
      default -> throw new IllegalStateException("Unexpected value: " + registryAccessKey.getKey());
    };
  }
}
