package org.aincraft;

import com.google.inject.Inject;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.events.PacketBlockBreakEvent;
import org.aincraft.events.PacketBlockInteractEvent;
import org.aincraft.events.PacketBlockPlaceEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus.Internal;

final class BlockController implements Listener {

  private final Plugin plugin;
  private final PacketBlockService blockService;
  private final ItemService itemService;

  @Inject
  BlockController(Plugin plugin, PacketBlockService blockService,
      ItemService itemService) {
    this.plugin = plugin;
    this.blockService = blockService;
    this.itemService = itemService;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPlayerChunkLoad(final PlayerChunkLoadEvent event) {
    Chunk chunk = event.getChunk();
    Player player = event.getPlayer();
    List<PacketBlock> blocks = blockService.loadAll(chunk);
    for (PacketBlock block : blocks) {
      BlockModel model = block.model();
      model.show(player);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPlayerChunkUnload(final PlayerChunkUnloadEvent event) {
    Chunk chunk = event.getChunk();
    Player player = event.getPlayer();
    List<PacketBlock> blocks = blockService.loadAll(chunk);
    for (PacketBlock block : blocks) {
      BlockModel model = block.model();
      model.hide(player);
    }
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
    Player player = event.getPlayer();
    Bukkit.getPluginManager().callEvent(new PacketBlockInteractEvent(
        player, block,
        event.getAction(), event.getHand(),
        event.getBlockFace(), event.getItem(), packetBlock.getMeta().key().toString())
    );
    BlockModel model = packetBlock.model();
    Bukkit.getScheduler().runTask(plugin, () -> {
      for (Player viewer : model.viewers()) {
        viewer.sendBlockChange(location, Bukkit.createBlockData(Material.GLASS));
      }
    });
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockPlace(final BlockPlaceEvent event) {
    ItemStack itemInHand = event.getItemInHand();
    Optional<String> data = itemService.read(itemInHand);
    if (data.isEmpty()) {
      return;
    }
    String resourceKey = data.get();
    Block block = event.getBlock();
    Player player = event.getPlayer();
    PacketBlockPlaceEvent blockPlaceEvent = new PacketBlockPlaceEvent(player, block, resourceKey);
    Bukkit.getPluginManager().callEvent(blockPlaceEvent);
    if (blockPlaceEvent.isCancelled()) {
      event.setCancelled(true);
      return;
    }
    Location location = block.getLocation();
    PacketBlock packetBlock = blockService.save(
        new BlockBindingImpl(location, resourceKey));
    BlockModel model = packetBlock.model();
    Chunk chunk = location.getChunk();
    for (Player viewer : chunk.getPlayersSeeingChunk()) {
      model.show(viewer);
    }
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      for (Player viewer : chunk.getPlayersSeeingChunk()) {
        viewer.sendBlockChange(block.getLocation(),Bukkit.createBlockData(Material.GLASS));
      }
    }, 2L);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockBreak(final BlockBreakEvent event) {
    Block block = event.getBlock();
    Location location = block.getLocation();
    PacketBlock packetBlock = blockService.load(location);
    if (packetBlock == null) {
      return;
    }
    Player player = event.getPlayer();
    PacketBlockMeta meta = packetBlock.getMeta();
    Key key = meta.key();
    PacketBlockBreakEvent blockBreakEvent = new PacketBlockBreakEvent(player, block,
        key.toString());
    Bukkit.getPluginManager().callEvent(blockBreakEvent);
    if (blockBreakEvent.isCancelled()) {
      event.setCancelled(true);
      return;
    }
    blockService.delete(location);
    meta.getSoundEntry(SoundType.BREAK).ifPresent(sound -> sound.play(location));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPistonBlockPush(final BlockPistonExtendEvent event) {
    handlePistonMoveEvent(new PistonEvent(event.getDirection(), event.getBlocks()));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onPistonBlockRetract(final BlockPistonRetractEvent event) {
    handlePistonMoveEvent(new PistonEvent(event.getDirection(), event.getBlocks()));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onEntityBlockBreak(final EntityExplodeEvent event) {
    handleExplosionEvent(event::blockList);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockExplodeEvent(final BlockExplodeEvent event) {
    handleExplosionEvent(event::blockList);
  }

  private void handlePistonMoveEvent(final PistonEvent event) {
    BlockFace blockFace = event.blockFace();
    Vector direction = blockFace.getDirection();
    List<Block> blockList = event.blockList();
    blockList.forEach(block -> {
      Location location = block.getLocation();
      PacketBlock packetBlock = blockService.load(location);
      Location newLocation = location.add(direction);
    });
  }

  private void handleExplosionEvent(final ExplodeEvent event) {
    for (Block block : event.getBlockList()) {
      Location location = block.getLocation();
      PacketBlock packetBlock = blockService.load(location);
      if (packetBlock == null) {
        continue;
      }
      blockService.delete(location);
    }
  }

  @Internal
  record PistonEvent(BlockFace blockFace, List<Block> blockList) {

  }

  @Internal
  interface ExplodeEvent {

    List<Block> getBlockList();
  }
}
