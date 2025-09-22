package org.aincraft.events;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketBlockInteractEvent extends PacketBlockEvent implements Cancellable {

  private static final @NotNull HandlerList HANDLERS = new HandlerList();

  private final @NotNull Action action;
  private final @Nullable EquipmentSlot hand;
  private final @Nullable BlockFace face;
  private final @Nullable ItemStack item;
  private boolean cancelled;

  @ApiStatus.Internal
  public PacketBlockInteractEvent(
      @NotNull Player player,
      @NotNull Block block,
      @NotNull Action action,
      @Nullable EquipmentSlot hand,
      @Nullable BlockFace face,
      @Nullable ItemStack item,
      @NotNull String resourceKey
  ) {
    super(player, block, resourceKey);
    this.action = action;
    this.hand = hand;
    this.face = face;
    this.item = item;
  }

  public @NotNull Action getAction() {
    return action;
  }

  public @Nullable EquipmentSlot getHand() {
    return hand;
  }

  public @Nullable BlockFace getFace() {
    return face;
  }

  public @Nullable ItemStack getItem() {
    return item;
  }

  public boolean isLeftClick() {
    return action == Action.LEFT_CLICK_BLOCK;
  }

  public boolean isRightClick() {
    return action == Action.RIGHT_CLICK_BLOCK;
  }

  public boolean isPhysical() {
    return action == Action.PHYSICAL;
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
