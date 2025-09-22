package org.aincraft;

import com.google.inject.Inject;
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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
    Bukkit.getPluginManager().callEvent(
        new PacketBlockInteractEvent(event.getPlayer(), block, event.getAction(), event.getHand(),
            event.getBlockFace(), event.getItem(), packetBlock.getMeta().key().toString()));
  }

  @EventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
  private void onCLick(final PacketBlockInteractEvent event) {
    String resourceKey = event.getResourceKey();

    Bukkit.broadcastMessage(resourceKey.toString());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockPlace(final BlockPlaceEvent event) {
    ItemStack itemInHand = event.getItemInHand();
    Optional<String> resourceKey = itemService.read(itemInHand);
    if (resourceKey.isEmpty()) {
      return;
    }
    Bukkit.broadcastMessage("here");
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
}
