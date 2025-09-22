package org.aincraft;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
      EntityModel model = EntityModel.create(EntityType.SHULKER, player.getLocation());
      model.showTo(player);
      Bukkit.getScheduler().runTaskLater(plugin, () -> {
        model.move(player.getLocation().clone().add(50,0,0));
      },10L);
    }
    return true;
  }
}
