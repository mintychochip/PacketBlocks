package org.aincraft;

import com.google.inject.Inject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {

  private final Plugin plugin;

  @Inject
  public TestCommand(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command,
      @NotNull String s, @NotNull String @NotNull [] strings) {
    if (commandSender instanceof Player player) {
      CraftPlayer craftPlayer = (CraftPlayer) player;
//      List<BlockBindingImpl> bindings = service.getBindings(player.getChunk());
//      ModelData data = new ModelDataImpl(
//          Key.key("item:bus")).translation(new Vector3f(0.5f, 0.5f, 0.5f))
//          .itemModel(Key.key("minecraft:diamond_ore"));
//
//      for (int n = 0; n < 100000; ++n) {
//        int i = ThreadLocalRandom.current().nextInt(100); // 0–99
//        int j = ThreadLocalRandom.current().nextInt(100);  // 0–9
//        int u = ThreadLocalRandom.current().nextInt(100);  // 0–9
//        Block block = player.getLocation().getBlock();
//        BlockModelImpl model = BlockModelImpl.create(data, player.getWorld(),
//            player.getLocation().clone().add(i, j, u).toVector());
//        model.show(player);
//      }
    }
    return true;
  }
}
