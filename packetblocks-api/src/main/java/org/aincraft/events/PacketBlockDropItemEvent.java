package org.aincraft.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PacketBlockDropItemEvent extends PacketBlockEvent implements Cancellable {

  public PacketBlockDropItemEvent(Player player, Block block,
      String resourceKey) {
    super(player, block, resourceKey);
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public void setCancelled(boolean b) {

  }
}
