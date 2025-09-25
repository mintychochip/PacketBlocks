package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ItemCommand implements CommandExecutor {

  private final Plugin plugin;
  private final ItemService itemService;

  @Inject
  public ItemCommand(Plugin plugin, ItemService itemService) {
    this.plugin = plugin;
    this.itemService = itemService;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      ItemStack stack = ItemStack.of(Material.STONE);
      itemService.write(stack,"packetblocks:electrum_ore");
      stack.setData(DataComponentTypes.ITEM_MODEL, Key.key("packetblocks:electrum_ore"));
      player.getInventory().addItem(stack);
    }
    return true;
  }
}
