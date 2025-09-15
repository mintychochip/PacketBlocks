package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
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

  @Inject
  public ItemCommand(PacketItemService itemService) {
    this.itemService = itemService;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      ItemStack stack = ItemStack.of(Material.STONE);
      ItemMeta itemMeta = stack.getItemMeta();
      stack.setData(DataComponentTypes.ITEM_NAME, Component.text("Electrum Ore"));
      stack.setData(DataComponentTypes.ITEM_MODEL, Key.key("packetblocks:electrum_ore"));
      itemService.writePacketData(stack,"item:bus");
      player.getInventory().addItem(stack);

    }
    return true;
  }
}
