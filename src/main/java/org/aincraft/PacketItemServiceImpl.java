package org.aincraft;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.aincraft.api.ClientBlockData;
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

  @Override
  public boolean isPacketItem(ItemStack stack) {
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    return pdc.has(itemKey);
  }

  @Override
  public @NotNull ClientBlockData readPacketData(ItemStack stack) throws IllegalArgumentException {
    Preconditions.checkArgument(isPacketItem(stack));
    PersistentDataContainerView pdc = stack.getPersistentDataContainer();
    String json = pdc.get(itemKey, PersistentDataType.STRING);
    return gson.fromJson(json, ClientBlockData.class);
  }

  @Override
  public void writePacketData(ItemStack stack, ClientBlockData data) {
    String json = gson.toJson(data);
    stack.editPersistentDataContainer(pdc -> {
      pdc.set(itemKey, PersistentDataType.STRING, json);
    });
  }
}
