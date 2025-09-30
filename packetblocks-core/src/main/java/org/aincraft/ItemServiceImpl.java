package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.persistence.PersistentDataContainerView;
import java.util.Optional;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class ItemServiceImpl implements ItemService {

  private static final String PACKET_ITEM_KEY_STRING = "packet_item";
  private final NamespacedKey itemKey;

  @Inject
  ItemServiceImpl(Plugin plugin) {
    this.itemKey = new NamespacedKey(plugin, PACKET_ITEM_KEY_STRING);
  }

  @Override
  public Optional<String> read(ItemStack itemStack) {
    if (invalidItem(itemStack)) {
      return Optional.empty();
    }
    PersistentDataContainerView pdc = itemStack.getPersistentDataContainer();
    if (!pdc.has(itemKey)) {
      return Optional.empty();
    }
    return Optional.ofNullable(pdc.get(itemKey, PersistentDataType.STRING));
  }

  @Override
  public void write(ItemStack itemStack, String resourceKey) {
    if (invalidItem(itemStack)) {
      return;
    }
    itemStack.editPersistentDataContainer(
        p -> p.set(itemKey, PersistentDataType.STRING, resourceKey));
  }

  private static boolean invalidItem(ItemStack itemStack) {
    return itemStack.getType().isAir() || itemStack.getAmount() <= 0;
  }
}
