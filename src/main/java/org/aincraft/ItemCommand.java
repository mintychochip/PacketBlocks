package org.aincraft;

import com.google.inject.Inject;
import org.aincraft.domain.PacketBlockDataRepository;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
      World world = player.getWorld();
//      CraftWorld craftWorld = (CraftWorld) world;
//      Vector vector = player.getLocation().toVector();
//      EntityModelImpl<Shulker> model = new EntityModelImpl<>(
//          craftWorld.getHandle(),
//          new Vec3(vector.getX(), vector.getY(), vector.getZ()), new Shulker(
//          EntityType.SHULKER, craftWorld.getHandle()));
//      CraftPlayer p = (CraftPlayer) player;
//      model.showTo(p.getHandle());
//      model.setInvisible(true);
//      model.setGlowing(true);
    }
    return true;
  }
}
