package org.aincraft.domain;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import java.util.List;
import org.aincraft.PacketItemService;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.aincraft.api.PacketBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

final class BlockController implements Listener {

  private final Plugin plugin;
  private final PacketItemService packetItemService;
  private final BlockBindingFactory blockBindingFactory;
  private final PacketBlockService blockService;
  private final Interceptor interceptor;
  private final Service service;

  @Inject
  BlockController(Plugin plugin, PacketItemService packetItemService,
      BlockBindingFactory blockBindingFactory,
      PacketBlockService blockService, Interceptor interceptor,
      Service service) {
    this.plugin = plugin;
    this.packetItemService = packetItemService;
    this.blockBindingFactory = blockBindingFactory;
    this.blockService = blockService;
    this.interceptor = interceptor;
    this.service = service;
  }

  @EventHandler
  private void onPlayerChunkLoad(final PlayerChunkLoadEvent event) {
    Player player = event.getPlayer();
    List<BlockBinding> bindings = service.getBindings(event.getChunk());
    for (BlockBinding binding : bindings) {
      PacketBlock block = blockService.load(binding.location());
      if (block == null) {
        continue;
      }
      BlockModel model = block.blockModel();
      model.show(player);
    }
  }

//  @EventHandler
//  private void onPlayerUnload(final PlayerChunkUnloadEvent event) {
//    Player player = event.getPlayer();
//    List<BlockBinding> bindings = service.getBindings(event.getChunk());
//    for (BlockBinding binding : bindings) {
//      BlockModel block = blockBindingService.loadBlock(binding.location());
//      if (block == null) {
//        continue;
//      }
//      block.hide(player);
//    }
//  }

  @EventHandler
  private void onInteractWithPacketBlock(final PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    if (block == null) {
      return;
    }
    PacketBlock packetBlock = blockService.load(block.getLocation());
    if (packetBlock == null) {
      return;
    }
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTask(plugin, () -> {
      player.sendBlockChange(block.getLocation(), Bukkit.createBlockData(Material.CHORUS_FLOWER));
    });
  }

  @EventHandler
  private void onPacketBlockPlace(final BlockPlaceEvent event) {
    final long t0 = System.nanoTime();

    ItemStack item = event.getItemInHand();
    Player player = event.getPlayer();
    Block serverBack = event.getBlockPlaced();

    if (!service.isPacketItem(item)) {
      return;
    }
    final long t1 = System.nanoTime();
    serverBack.setType(Material.STONE);
    String resourceKey = service.readPacketData(item);
    final long t2 = System.nanoTime();

    BlockBinding binding = blockBindingFactory.bind(serverBack.getLocation(), resourceKey);
    PacketBlock packetBlock = blockService.save(binding);
    packetBlock.soundData().getEntry(SoundType.PLACE).play(player);
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      BlockData fake = Bukkit.createBlockData(Material.CHORUS_FLOWER);
      player.sendBlockChange(serverBack.getLocation(), fake);
    }, 2L);
    final long t3 = System.nanoTime();

    packetBlock.blockModel().show(player);
    final long t4 = System.nanoTime();

    // Log (replace with your logger)
    double msIsPacket = (t1 - t0) / 1_000_000.0;
    double msRead = (t2 - t1) / 1_000_000.0;
    double msUpsert = (t3 - t2) / 1_000_000.0;
    double msShow = (t4 - t3) / 1_000_000.0;
    double msTotal = (t4 - t0) / 1_000_000.0;

    Bukkit.getLogger().info(String.format(
        java.util.Locale.ROOT,
        "BlockPlace timings: isPacketItem=%.3f ms, readPacketData=%.3f ms, upsert=%.3f ms, show=%.3f ms, total=%.3f ms",
        msIsPacket, msRead, msUpsert, msShow, msTotal
    ));
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
    ItemStack itemStack = player.getInventory().getItem(EquipmentSlot.HAND);
    PacketBlock packetBlock = blockService.load(location);
    if (packetBlock != null) {
      blockService.delete(location);
      SoundData soundData = packetBlock.soundData();
      soundData.getEntry(SoundType.BREAK).play(player);
      if (itemStack.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) {
        Material material = block.getType();
        ItemStack stack = ItemStack.of(material);
        stack.setData(DataComponentTypes.ITEM_MODEL,packetBlock.modelData().itemModel());
        packetItemService.writePacketData(stack, packetBlock.modelData().resourceKey().toString());
        location.getWorld().dropItemNaturally(location,stack);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  private void onPacketBlockEntityBreak(final EntityExplodeEvent event) {
    List<Block> blocks = event.blockList();
    blocks.forEach(block -> {
      PacketBlock packetBlock = blockService.load(block.getLocation());
      if (packetBlock != null) {
        blockService.delete(block.getLocation());
      }
    });
  }

}
