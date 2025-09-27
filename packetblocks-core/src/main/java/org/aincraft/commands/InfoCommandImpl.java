package org.aincraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class InfoCommandImpl implements PacketBlockCommand{

  @Override
  public LiteralArgumentBuilder<CommandSourceStack> build() {
    return Commands.literal("info")
        .executes(context -> {
          CommandSender sender = context.getSource().getSender();
          if (!(sender instanceof Player player)) {
            sender.sendMessage("Must be a player");
            return 1;
          }
          return 0;
        });
  }
}
