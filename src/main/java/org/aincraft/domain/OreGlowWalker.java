package org.aincraft.domain;

import com.google.inject.Inject;
import org.aincraft.api.EntityModelImpl;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class OreGlowWalker implements Listener {

  // Per-player: map of block-position keys to active models
  private final Map<UUID, Map<Long, EntityModelImpl<Shulker>>> models = new ConcurrentHashMap<>();

  private final int radius;              // scan radius in blocks
  private final int maxSpawnsPerScan;    // safety cap per scan (avoid spikes)

  @Inject
  public OreGlowWalker() {
    this.radius = Math.max(1, 50);
    this.maxSpawnsPerScan = Math.max(1, 30);
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    final Player player = event.getPlayer();

    // ignore tiny head turns / sub-block wiggles
    if (event.getFrom().distanceSquared(event.getTo()) < 0.25) return;

    // set up lookup maps
    Map<Long, EntityModelImpl<Shulker>> mine = models.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());

    final World world = player.getWorld();
    final CraftWorld cworld = (CraftWorld) world;
    final ServerLevel level = cworld.getHandle();
    final ServerPlayer sPlayer = ((CraftPlayer) player).getHandle();

    // scan a small cube around the player
    final Vector base = player.getLocation().toVector();
    final int bx = base.getBlockX();
    final int by = base.getBlockY();
    final int bz = base.getBlockZ();

    // We’ll collect which keys should remain this tick
    final Set<Long> stillInRange = new HashSet<>();
    int spawned = 0;

    for (int x = bx - radius; x <= bx + radius; x++) {
      for (int y = by - radius; y <= by + radius; y++) {
        for (int z = bz - radius; z <= bz + radius; z++) {
          // quick radius check (sphere-ish)
          if ((x - bx) * (x - bx) + (y - by) * (y - by) + (z - bz) * (z - bz) > radius * radius) {
            continue;
          }

          Material mat = world.getBlockAt(x, y, z).getType();
          if (!isOre(mat)) continue;

          long key = BlockPos.asLong(x, y, z);
          stillInRange.add(key);

          // already created for this player? keep it
          if (mine.containsKey(key)) continue;

          if (spawned >= maxSpawnsPerScan) continue; // cap per step

          // center of the block
          Vec3 pos = new Vec3(x + 0.5, y + 0.5, z + 0.5);

          // create NMS shulker + model
          Shulker shulker = new Shulker(EntityType.SHULKER, level);
          shulker.setNoAi(true);
          shulker.setSilent(true);
          shulker.setInvulnerable(true);

          EntityModelImpl<Shulker> model = new EntityModelImpl<>(level, pos, shulker);

          // client-spawn and style
          model.showTo(sPlayer);
          model.setInvisible(true);
          model.setGlowing(true);

          mine.put(key, model);
          spawned++;
        }
      }
    }

    // remove those that are no longer in range
    if (!mine.isEmpty()) {
      Iterator<Map.Entry<Long, EntityModelImpl<Shulker>>> it = mine.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<Long, EntityModelImpl<Shulker>> e = it.next();
        if (!stillInRange.contains(e.getKey())) {
          e.getValue().hideFrom(sPlayer);
          it.remove();
        }
      }
    }
  }

  private static boolean isOre(Material m) {
    // vanilla + deepslate + nether ores
    switch (m) {
      case COAL_ORE:
      case IRON_ORE:
      case COPPER_ORE:
      case GOLD_ORE:
      case REDSTONE_ORE:
      case EMERALD_ORE:
      case LAPIS_ORE:
      case DIAMOND_ORE:
      case DEEPSLATE_COAL_ORE:
      case DEEPSLATE_IRON_ORE:
      case DEEPSLATE_COPPER_ORE:
      case DEEPSLATE_GOLD_ORE:
      case DEEPSLATE_REDSTONE_ORE:
      case DEEPSLATE_EMERALD_ORE:
      case DEEPSLATE_LAPIS_ORE:
      case DEEPSLATE_DIAMOND_ORE:
      case NETHER_GOLD_ORE:
      case NETHER_QUARTZ_ORE:
        return true;
      default:
        return false;
    }
  }

  // Optional helper to clean up when player quits (recommended)
  public void unregisterPlayer(Player p) {
    Map<Long, EntityModelImpl<Shulker>> mine = models.remove(p.getUniqueId());
    if (mine == null) return;
    ServerPlayer sPlayer = ((CraftPlayer) p).getHandle();
    for (EntityModelImpl<Shulker> model : mine.values()) {
      model.hideFrom(sPlayer);
    }
  }
}
