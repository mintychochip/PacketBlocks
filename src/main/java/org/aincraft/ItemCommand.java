package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.aincraft.domain.ClientBlockDataImpl;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

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
      ItemStack stack = ItemStack.of(Material.MANGROVE_ROOTS);
      stack.setData(DataComponentTypes.ITEM_MODEL, Key.key("minecraft:diamond_ore"));
      itemService.writePacketData(stack,
          new ClientBlockDataImpl(Key.key("item:bus")).translation(new Vector3f(0.5f, 0.5f, 0.5f))
              .itemModel(Key.key("minecraft:diamond_ore")));
      player.getInventory().addItem(stack);

    }
    return true;
  }
}
