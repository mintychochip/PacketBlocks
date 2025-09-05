package org.aincraft;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

  private final Plugin plugin;

  public TestCommand(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String @NotNull [] strings) {
    if (commandSender instanceof Player player) {
      CraftPlayer craftPlayer = (CraftPlayer) player;
      ServerPlayer serverPlayer = craftPlayer.getHandle();
      Random random = new Random();
      Location location = player.getLocation().clone();
      for (int i = 0; i < 15000; ++i) {
        Vector vector = new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble());
        vector.multiply(50);
        ClientBlock block = ClientBlockImpl.create(player.getWorld(),
            location.clone().add(vector).toVector(), plugin);
        block.show(player);
      }
    }
    return true;
  }
}
