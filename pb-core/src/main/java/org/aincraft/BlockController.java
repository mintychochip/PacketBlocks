package org.aincraft;

import com.google.inject.Inject;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.events.PacketBlockInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;

final class BlockController implements Listener {

  private final PacketBlockService blockService;
  private final ItemService itemService;

  @Inject
  BlockController(PacketBlockService blockService, ItemService itemService) {
    this.blockService = blockService;
    this.itemService = itemService;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockInteract(final PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    if (block == null) {
      return;
    }
    Location location = block.getLocation();
    PacketBlock packetBlock = blockService.load(location);
    if (packetBlock == null) {
      return;
    }
    PacketBlockInteractEvent packetBlockInteractEvent = new PacketBlockInteractEvent(
        event.getPlayer(), block,
        event.getAction(), event.getHand(),
        event.getBlockFace(), event.getItem(), packetBlock.getMeta().key().toString());
    Bukkit.getPluginManager().callEvent(packetBlockInteractEvent
    );
    if (packetBlockInteractEvent.isCancelled()) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockPlace(final BlockPlaceEvent event) {
    ItemStack itemInHand = event.getItemInHand();
    Optional<String> resourceKey = itemService.read(itemInHand);
    if (resourceKey.isEmpty()) {
      return;
    }
    Block block = event.getBlock();
    PacketBlock packetBlock = blockService.save(
        BlockBinding.create(block.getLocation(), resourceKey.get()));
    //TODO: handle for players around the block
    EntityModel model = packetBlock.getModel();
    model.showTo(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockBreak(final BlockBreakEvent event) {
    Block block = event.getBlock();
    Location location = block.getLocation();
    PacketBlock packetBlock = blockService.load(location);
    if (packetBlock == null) {
      return;
    }
    blockService.delete(location);
    PacketBlockMeta meta = packetBlock.getMeta();
    meta.getSoundEntry(SoundType.BREAK).ifPresent(sound -> sound.play(location));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onEntityBlockBreak(final EntityExplodeEvent event) {
    handleExplosionEvent(event::blockList);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockExplodeEvent(final BlockExplodeEvent event) {
    handleExplosionEvent(event::blockList);
  }

  private void handleExplosionEvent(final ExplodeEvent event) {
    List<Block> blocks = event.getBlockList();
    blocks.forEach(block -> {
      Location location = block.getLocation();
      PacketBlock packetBlock = blockService.load(location);
      if (packetBlock != null) {
        blockService.delete(location);
      }
    });
  }

  @Internal
  interface ExplodeEvent {
    List<Block> getBlockList();
  }
}
