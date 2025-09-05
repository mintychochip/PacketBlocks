package org.aincraft;

import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.EntityType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String @NotNull [] strings) {
    if (commandSender instanceof Player player) {
      CraftPlayer craftPlayer = (CraftPlayer) player;
      ServerPlayer serverPlayer = craftPlayer.getHandle();
      ClientBlock clientBlock = ClientBlockImpl.create(Key.key("minecraft:diamond_ore"),
          player.getLocation(), serverPlayer.level());
      clientBlock.show(player);
    }
    return true;
  }
}
