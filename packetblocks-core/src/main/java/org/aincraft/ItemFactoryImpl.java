package org.aincraft;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.aincraft.registry.Registry;
import org.bukkit.inventory.ItemStack;

final class ItemFactoryImpl implements ItemFactory {

  private final ItemService itemService;
  private final Registry<BlockItemMeta> blockItemMetaRegistry;

  @Inject
  public ItemFactoryImpl(ItemService itemService,
      Registry<BlockItemMeta> blockItemMetaRegistry) {
    this.itemService = itemService;
    this.blockItemMetaRegistry = blockItemMetaRegistry;
  }

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public ItemStack create(Key resourceKey, int amount) throws IllegalArgumentException {
    Preconditions.checkArgument(blockItemMetaRegistry.isRegistered(resourceKey));
    BlockItemMeta blockItemMeta = blockItemMetaRegistry.get(resourceKey);
    ItemStack stack = ItemStack.of(blockItemMeta.material(), amount);
    stack.setData(DataComponentTypes.ITEM_MODEL, blockItemMeta.itemModel());
    Component displayName = blockItemMeta.displayName();
    if (displayName != null) {
      stack.setData(DataComponentTypes.ITEM_NAME, displayName);
    }
    List<? extends Component> itemLore = blockItemMeta.itemLore();
    if (itemLore != null && !itemLore.isEmpty()) {
      stack.setData(DataComponentTypes.LORE, ItemLore.lore().addLines(itemLore));
    }

    itemService.write(stack, resourceKey.toString());
    return stack;
  }
}
