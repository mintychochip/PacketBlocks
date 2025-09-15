package org.aincraft.domain;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aincraft.PacketItemService;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.aincraft.api.ItemData;
import org.aincraft.api.PacketBlock;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.SoundData;
import org.aincraft.api.SoundData.SoundType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

final class BlockController implements Listener {

  private final Plugin plugin;
  private final BlockBindingRepository blockBindingRepository;
  private final PacketItemService packetItemService;
  private final PacketBlockService blockService;
  private final Service service;

  private final Map<Location, PacketBlockData> recentlyDeleted = new HashMap<>();

  @Inject
  BlockController(Plugin plugin, BlockBindingRepository blockBindingRepository,
      PacketItemService packetItemService,
      PacketBlockService blockService,
      Service service) {
    this.plugin = plugin;
    this.blockBindingRepository = blockBindingRepository;
    this.packetItemService = packetItemService;
    this.blockService = blockService;
    this.service = service;
  }

  @EventHandler
  private void onPlayerChunkLoad(final PlayerChunkLoadEvent event) {
    Player player = event.getPlayer();
    List<BlockBinding> bindings = blockBindingRepository.loadAllByChunk(event.getChunk());
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
//    List<BlockBindingImpl> bindings = service.getBindings(event.getChunk());
//    for (BlockBindingImpl binding : bindings) {
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
      player.sendBlockChange(block.getLocation(), Bukkit.createBlockData(Material.GLASS));
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

    PacketBlock packetBlock = blockService.save(
        BlockBinding.create(serverBack.getLocation(), resourceKey));
    packetBlock.blockData().soundData().entry(SoundType.PLACE).play(serverBack.getLocation());
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      BlockData fake = Bukkit.createBlockData(Material.GLASS);
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

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  private void onPacketBlockBreak(final BlockBreakEvent event) {
    Block block = event.getBlock();
    Location location = block.getLocation();
    PacketBlock packetBlock = blockService.load(location);
    if (packetBlock != null) {
      blockService.delete(location);
      recentlyDeleted.put(location, packetBlock.blockData());
      PacketBlockData blockData = packetBlock.blockData();
      SoundData soundData = blockData.soundData();
      soundData.entry(SoundType.BREAK).play(block.getLocation());
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  private void onBlockDropItem(final BlockDropItemEvent event) {
    List<Item> items = event.getItems();
    if (items.isEmpty()) {
      return;
    }
    Block block = event.getBlock();
    if (!recentlyDeleted.containsKey(block.getLocation())) {
      return;
    }
    Player player = event.getPlayer();
    PlayerInventory inventory = player.getInventory();
    ItemStack itemStack = inventory.getItemInMainHand();
    PacketBlockData blockData = recentlyDeleted.remove(block.getLocation());
    if (!items.isEmpty() && itemStack.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0) {
      ItemData itemData = blockData.itemData();
      ItemStack stack = ItemStack.of(itemData.material());
      stack.setData(DataComponentTypes.ITEM_MODEL, itemData.itemModel());
      stack.setData(DataComponentTypes.ITEM_NAME,itemData.displayName());
      packetItemService.writePacketData(stack, blockData.resourceKey().toString());
      items.getFirst().setItemStack(stack);
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
