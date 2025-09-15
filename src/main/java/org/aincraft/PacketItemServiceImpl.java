package org.aincraft;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PacketItemServiceImpl implements PacketItemService {

  private final NamespacedKey itemKey;
  private final Gson gson;

  public PacketItemServiceImpl(NamespacedKey itemKey, Gson gson) {
    this.itemKey = itemKey;
    this.gson = gson;
  }

  public boolean isPacketItem(ItemStack stack) {
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    return pdc.has(itemKey);
  }

  @Override
  public @NotNull String readPacketData(ItemStack stack) throws IllegalArgumentException {
    Preconditions.checkArgument(isPacketItem(stack));
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    return pdc.get(itemKey, PersistentDataType.STRING);
  }

  @Override
  public void writePacketData(ItemStack stack, String resourceKey) {
    stack.editPersistentDataContainer(pdc -> {
      pdc.set(itemKey, PersistentDataType.STRING, resourceKey);
    });
  }
}
