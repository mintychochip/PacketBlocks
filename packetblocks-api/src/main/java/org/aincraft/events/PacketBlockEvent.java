package org.aincraft.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PacketBlockEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();

  private final Player player;
  private final Block block;
  private final String resourceKey;

  public PacketBlockEvent(Player player, Block block, String resourceKey) {
    super();
    this.player = player;
    this.block = block;
    this.resourceKey = resourceKey;
  }

  public Player getPlayer() {
    return player;
  }

  public Block getBlock() {
    return block;
  }

  public String getResourceKey() {
    return resourceKey;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
