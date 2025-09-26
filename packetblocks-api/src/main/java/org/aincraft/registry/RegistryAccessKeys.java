package org.aincraft.registry;

import org.aincraft.KeyedItem;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.RegistryAccess.RegistryAccessKey;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

@AvailableSince("1.0.2")
public final class RegistryAccessKeys {

  public static final RegistryAccessKey<PacketBlockMeta> PACKET_BLOCK_META = () -> "packet_block_meta";
  public static final RegistryAccessKey<KeyedItem> ITEM = () -> "keyed_item";

  private RegistryAccessKeys() {
    throw new UnsupportedOperationException("do not instantiate");
  }
}
