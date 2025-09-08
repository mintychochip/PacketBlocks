package org.aincraft;

import com.google.inject.Inject;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.aincraft.domain.ModelDataImpl;
import org.aincraft.domain.ClientBlockService;
import org.aincraft.domain.Service;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class TestCommand implements CommandExecutor {

  private final Plugin plugin;
  private final ClientBlockService clientBlockService;
  private final Service service;

  @Inject
  public TestCommand(Plugin plugin, ClientBlockService clientBlockService, Service service) {
    this.plugin = plugin;
    this.clientBlockService = clientBlockService;
    this.service = service;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String @NotNull [] strings) {
    if (commandSender instanceof Player player) {
      CraftPlayer craftPlayer = (CraftPlayer) player;
      List<BlockBinding> bindings = service.getBindings(player.getChunk());
      for (int i = 0; i < 25; ++i) {
        for (int j = 0; j < 25; ++j) {
          for (int u = 0; u < 25; ++u) {
            BlockModel block = clientBlockService.upsertBlock(
                new BlockBindingImpl(new ModelDataImpl(
                    Key.key("item:bus")).translation(new Vector3f(0.5f, 0.5f, 0.5f))
                    .itemModel(Key.key("minecraft:diamond_ore")),
                    player.getLocation().clone().add(i, j, u)));
            block.show(player);
          }
        }
      }
    }
    return true;
  }
}
