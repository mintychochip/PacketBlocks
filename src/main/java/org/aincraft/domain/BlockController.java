package org.aincraft.domain;

import com.google.inject.Inject;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.aincraft.PacketItemService;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockModel;
import org.aincraft.api.ItemData;
import org.aincraft.api.PacketBlock;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.SoundData;
import org.aincraft.api.SoundData.SoundType;
import org.aincraft.api.SoundEntry;
import org.aincraft.loot.ItemLootInstanceImpl;
import org.aincraft.loot.Loot;
import org.aincraft.loot.Loot.LootInstance;
import org.aincraft.loot.LootContext;
import org.aincraft.loot.triggers.TriggerOnBreak;
import org.aincraft.loot.triggers.TriggerOnDrop;
import org.aincraft.loot.LootContextImpl;
import org.aincraft.loot.LootData;
import org.aincraft.loot.triggers.TriggerOnBreak.BlockBreakContext;
import org.aincraft.loot.triggers.TriggerOnDrop.DropItemContext;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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

  private final Map<Location, List<LootInstance>> recentlyDeleted = new HashMap<>();

  @Inject
  BlockController(Plugin plugin, BlockBindingRepository blockBindingRepository,
      PacketItemService packetItemService,
      PacketBlockService blockService) {
    this.plugin = plugin;
    this.blockBindingRepository = blockBindingRepository;
    this.packetItemService = packetItemService;
    this.blockService = blockService;
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

    if (!packetItemService.isPacketItem(item)) {
      return;
    }
    final long t1 = System.nanoTime();
    serverBack.setType(Material.STONE);
    String resourceKey = packetItemService.readPacketData(item);
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
    if (packetBlock == null) {
      return;
    }
    Player player = event.getPlayer();
    PlayerInventory inventory = player.getInventory();
    ItemStack itemStack = inventory.getItemInMainHand();
    PacketBlockData blockData = packetBlock.blockData();
    List<LootInstance> instances = loot(blockData, new LootContextImpl(itemStack, player));
    if (player.getGameMode() != GameMode.CREATIVE) {
      BlockBreakContext context = breakContext(event);
      for (LootInstance instance : instances) {
        if (instance instanceof TriggerOnBreak triggerOnBreak) {
          triggerOnBreak.onBreak(context);
        }
      }
    }
    blockService.delete(location);
    recentlyDeleted.put(location, instances);
    SoundData soundData = blockData.soundData();
    SoundEntry soundEntry = soundData.entry(SoundType.BREAK);
    if (soundEntry != null) {
      soundEntry.play(block.getLocation());
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  private void onBlockDropItem(final BlockDropItemEvent event) {
    List<Item> items = event.getItems();
    if (items.isEmpty()) {
      return;
    }
    Bukkit.broadcastMessage("here");
    Block block = event.getBlock();
    List<LootInstance> instances = recentlyDeleted.remove(block.getLocation());
    if (instances == null) {
      return;
    }
    DropItemContext context = dropContext(event);
    for (LootInstance instance : instances) {
      if (instance instanceof TriggerOnDrop itemLoot) {
        itemLoot.onDrop(context);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  private void onPacketBlockEntityBreak(final EntityExplodeEvent event) {
    List<Block> blocks = event.blockList();
    blocks.forEach(block -> {
      Location location = block.getLocation();
      PacketBlock packetBlock = blockService.load(location);
      if (packetBlock != null) {
        PacketBlockData blockData = packetBlock.blockData();
        List<LootInstance> instances = loot(blockData, new LootContextImpl(null, null));
        List<ItemStack> itemList = new ArrayList<>();
        for (LootInstance instance : instances) {
          if (instance instanceof TriggerOnDrop drop) {
            drop.onDrop(new DropItemContext() {
              @Override
              public void items(List<ItemStack> items) {
                itemList.addAll(items);
              }

              @Override
              public List<ItemStack> items() {
                return itemList;
              }
            });
          }
        }
        itemList.forEach(item -> {
          location.getWorld().dropItem(location,item);
        });
        blockService.delete(location);
        SoundData soundData = blockData.soundData();
        SoundEntry soundEntry = soundData.entry(SoundType.BREAK);
        if (soundEntry != null) {
          soundEntry.play(location);
        }
        blocks.remove(block);
        block.setType(Material.AIR);
      }
    });
  }

  @SuppressWarnings("UnstableApiUsage")
  private ItemStack createPacketBlockItem(PacketBlockData packetBlockData) {
    ItemData itemData = packetBlockData.itemData();
    ItemStack stack = ItemStack.of(itemData.material());
    stack.setData(DataComponentTypes.ITEM_MODEL, itemData.itemModel());
    stack.setData(DataComponentTypes.ITEM_NAME, itemData.displayName());
    packetItemService.writePacketData(stack, packetBlockData.resourceKey().toString());
    return stack;
  }

  private List<LootInstance> loot(PacketBlockData packetBlockData, LootContext context) {
    LootData lootData = packetBlockData.lootData();
    if (context.silkTouch() && lootData.dropOnSilkTouch()) {
      ItemStack item = createPacketBlockItem(packetBlockData);
      return List.of(new ItemLootInstanceImpl(1, item.clone()));
    }
    return lootData.get(context);
  }

  private static BlockBreakContext breakContext(BlockBreakEvent event) {
    return new BlockBreakContext() {
      @Override
      public void exp(int xp) {
        event.setExpToDrop(xp);
      }

      @Override
      public int exp() {
        return event.getExpToDrop();
      }
    };
  }

  private static DropItemContext dropContext(BlockDropItemEvent event) {
    event.getItems().clear();
    return new DropItemContext() {
      @Override
      public void items(List<ItemStack> items) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        Location center = location.clone().add(0.5, 0.5, 0.5);
        World world = location.getWorld();
        List<Item> itemList = items.stream().map(item -> {
          Item i = world.createEntity(center, Item.class);
          i.setItemStack(item);
          return i;
        }).toList();
        event.getItems().clear();
        event.getItems().addAll(itemList);
      }

      @Override
      public List<ItemStack> items() {
        return event.getItems().stream().map(Item::getItemStack).collect(Collectors.toList());
      }
    };
  }
}
