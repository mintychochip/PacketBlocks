package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemCommand implements CommandExecutor {

  private final ItemService itemService;

  @Inject
  public ItemCommand(ItemService itemService) {
    this.itemService = itemService;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      ItemStack itemStack = ItemStack.of(Material.STONE);
      ItemService.itemService().write(itemStack,"longhardfish:block/gutting_station");
      itemStack.setData(DataComponentTypes.ITEM_MODEL, Key.key("longhardfish:block/gutting_station"));
      player.getInventory().addItem(itemStack);
    }
    return true;
  }
}
