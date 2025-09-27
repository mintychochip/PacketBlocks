package org.aincraft.registry;

import org.aincraft.BlockModelData;
import org.aincraft.KeyedItem;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.RegistryAccess.RegistryAccessKey;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

@AvailableSince("1.0.2")
public final class RegistryAccessKeys {

  public static final RegistryAccessKey<BlockModelData> BLOCK_MODEL_DATA = () -> "block_model_data";
  public static final RegistryAccessKey<KeyedItem> ITEM = () -> "keyed_item";

  private RegistryAccessKeys() {
    throw new UnsupportedOperationException("do not instantiate");
  }
}
