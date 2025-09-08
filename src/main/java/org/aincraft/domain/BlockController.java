package org.aincraft.domain;

import com.google.inject.Inject;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import org.aincraft.BlockBindingImpl;
import org.aincraft.ClientBlockImpl;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.ClientBlock;
import org.aincraft.api.ClientBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootContext.Builder;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;

final class BlockController implements Listener {

  private final Plugin plugin;
  private final ClientBlockService clientBlockService;
  private final Service service;

  @Inject
  BlockController(Plugin plugin, ClientBlockService clientBlockService, Service service) {
    this.plugin = plugin;
    this.clientBlockService = clientBlockService;
    this.service = service;
  }

  @EventHandler
  private void onPlayerChunkLoad(final PlayerChunkLoadEvent event) {
    Player player = event.getPlayer();
    List<BlockBinding> bindings = service.getBindings(event.getChunk());
    for (BlockBinding binding : bindings) {
      ClientBlock block = clientBlockService.loadBlock(binding.location());
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
      ClientBlock block = clientBlockService.loadBlock(binding.location());
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
      ClientBlockData data = service.readPacketData(item);
      ClientBlock block = clientBlockService.upsertBlock(
          new BlockBindingImpl(data, serverBack.getLocation()));
      block.show(player);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  private void onPacketBlockBreak(final BlockBreakEvent event) {
    Block block = event.getBlock();
    Location location = block.getLocation();
    Player player = event.getPlayer();
    ClientBlock clientBlock = clientBlockService.loadBlock(location);
    if (clientBlock == null) {
      return;
    }
    event.setCancelled(true);
    clientBlockService.deleteBlock(block.getLocation());
    block.setType(Material.AIR);
    LootTable table = Bukkit.getLootTable(NamespacedKey.fromString("blocks/diamond_ore"));
    LootContext context = new Builder(block.getLocation()).luck(1.0f).killer(player).build();
    Collection<ItemStack> stack = table.populateLoot(ThreadLocalRandom.current(), context);
    Bukkit.broadcastMessage(stack.toString());
  }
}
