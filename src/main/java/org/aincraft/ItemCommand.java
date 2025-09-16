package org.aincraft;

import com.google.inject.Inject;
import org.aincraft.api.EntityModel;
import org.aincraft.domain.PacketBlockDataRepository;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
      EntityModel entityModel = EntityModel.create(EntityType.ITEM_DISPLAY, player.getLocation());
      entityModel.showTo(player);
    }
    return true;
  }
}
