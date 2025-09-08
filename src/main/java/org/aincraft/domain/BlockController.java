package org.aincraft.domain;

import com.google.inject.Inject;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.aincraft.BlockBindingImpl;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.aincraft.api.ModelData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootContext.Builder;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;

final class BlockController implements Listener {

  private final Plugin plugin;
  private final Interceptor interceptor;
  private final ClientBlockService clientBlockService;
  private final Service service;

  @Inject
  BlockController(Plugin plugin, Interceptor interceptor, ClientBlockService clientBlockService, Service service) {
    this.plugin = plugin;
    this.interceptor = interceptor;
    this.clientBlockService = clientBlockService;
    this.service = service;
  }

  @EventHandler
  private void onPlayerChunkLoad(final PlayerChunkLoadEvent event) {
    Player player = event.getPlayer();
    List<BlockBinding> bindings = service.getBindings(event.getChunk());
    for (BlockBinding binding : bindings) {
      BlockModel block = clientBlockService.loadBlock(binding.location());
      if (block == null) {
        continue;
      }
      block.setBlockData(binding.blockData());
      block.show(player);
    }
  }

  @EventHandler
  private void onPlayerUnload(final PlayerChunkUnloadEvent event) {
    Player player = event.getPlayer();
    List<BlockBinding> bindings = service.getBindings(event.getChunk());
    for (BlockBinding binding : bindings) {
      BlockModel block = clientBlockService.loadBlock(binding.location());
      if (block == null) {
        continue;
      }
      block.hide(player);
    }
  }

  @EventHandler
  private void onPacketBlockPlace(final BlockPlaceEvent event) {
    ItemStack item = event.getItemInHand();
    Player player = event.getPlayer();
    Block serverBack = event.getBlockPlaced();
    boolean packetItem = service.isPacketItem(item);
    if (packetItem) {
      CraftWorld craftWorld = (CraftWorld) serverBack.getWorld();
      ModelData data = service.readPacketData(item);
      BlockModel block = clientBlockService.upsertBlock(
          new BlockBindingImpl(data, serverBack.getLocation()));
      block.show(player);
    }
  }

  @EventHandler
  private void onJoin(final PlayerJoinEvent event) {
    interceptor.inject(event.getPlayer());
  }

  @EventHandler
  private void onLeave(final PlayerQuitEvent event) {
    interceptor.eject(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  private void onPacketBlockBreak(final BlockBreakEvent event) {
    Block block = event.getBlock();
    Location location = block.getLocation();
    Player player = event.getPlayer();
    BlockModel blockModel = clientBlockService.loadBlock(location);
    if (blockModel == null) {
      return;
    }
    event.setCancelled(true);
    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK,1f,1f);
    Bukkit.broadcastMessage("here");
  }
}
