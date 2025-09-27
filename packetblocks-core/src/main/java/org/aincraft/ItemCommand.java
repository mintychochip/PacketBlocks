package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.aincraft.registry.Registry;
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
  private final Registry<BlockModelData> blockModelDataRegistry;
  private final ItemService itemService;

  @Inject
  public ItemCommand(Plugin plugin, Registry<BlockModelData> blockModelDataRegistry,
      ItemService itemService) {
    this.plugin = plugin;
    this.blockModelDataRegistry = blockModelDataRegistry;
    this.itemService = itemService;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
      @NotNull String label, @NotNull String @NotNull [] args) {
    if (sender instanceof Player player) {
      BlockModelData modelData = blockModelDataRegistry.get(Key.key("packetblocks:electrum_ore"));
      BlockModel model = BlockModel.create(player.getLocation().toBlockLocation(), modelData);
      model.show(player);
      Bukkit.getScheduler().runTaskLater(plugin,() -> {
        model.move(player.getLocation().clone().add(1,0,0));
      },10L);

    }
    return true;
  }
}
