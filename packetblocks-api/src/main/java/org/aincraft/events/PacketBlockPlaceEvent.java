package org.aincraft.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

@AvailableSince("1.0.2")
public final class PacketBlockPlaceEvent extends PacketBlockEvent implements Cancellable {

  private static final @NotNull HandlerList HANDLERS = new HandlerList();
  private boolean cancelled;

  @ApiStatus.Internal
  public PacketBlockPlaceEvent(
      @NotNull Player player,
      @NotNull Block block,
      @NotNull String resourceKey
  ) {
    super(player, block, resourceKey);
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLERS;
  }
}
