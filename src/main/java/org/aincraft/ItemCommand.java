package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.aincraft.api.ItemData;
import org.aincraft.api.PacketBlockData;
import org.aincraft.domain.PacketBlockDataRepository;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemCommand implements CommandExecutor {

  private final PacketItemService itemService;
  private final PacketBlockDataRepository blockDataRepository;

  @Inject
  public ItemCommand(PacketItemService itemService, PacketBlockDataRepository blockDataRepository) {
    this.itemService = itemService;
    this.blockDataRepository = blockDataRepository;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      ItemStack stack = ItemStack.of(Material.STONE);
      ItemMeta itemMeta = stack.getItemMeta();
      PacketBlockData data = blockDataRepository.load("metal:rose_quartz_ore");
      ItemData itemData = data.itemData();
      stack.setData(DataComponentTypes.ITEM_NAME, itemData.displayName());
      stack.setData(DataComponentTypes.ITEM_MODEL, itemData.itemModel());
      itemService.writePacketData(stack,data.resourceKey().toString());
      player.getInventory().addItem(stack);

    }
    return true;
  }
}
