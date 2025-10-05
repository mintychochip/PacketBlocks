package org.aincraft.registry;

import net.kyori.adventure.key.Keyed;
import org.aincraft.BlockItemMeta;
import org.aincraft.BlockModelData;
import org.aincraft.KeyedItem;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.RegistryAccess.RegistryAccessKey;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

@AvailableSince("1.0.2")
public final class RegistryAccessKeys {

  public static final RegistryAccessKey<BlockItemMeta> BLOCK_ITEM_META = key("block_item_meta");
  public static final RegistryAccessKey<BlockModelData> BLOCK_MODEL_DATA = key("block_model_data");
  public static final RegistryAccessKey<KeyedItem> ITEM = key("keyed_item");

  private static <T extends Keyed> RegistryAccessKey<T> key(String key) {
    return () -> key;
  }

  private RegistryAccessKeys() {
    throw new UnsupportedOperationException("do not instantiate");
  }
}
