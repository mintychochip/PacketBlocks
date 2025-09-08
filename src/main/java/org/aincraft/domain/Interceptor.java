package org.aincraft.domain;

import com.google.inject.Inject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Interceptor {

  private final Plugin plugin;

  @Inject
  public Interceptor(Plugin plugin) {
    this.plugin = plugin;
    LOG = plugin.getLogger();
  }


  private static final String HANDLER_PREFIX = "bus-";
  private final java.util.logging.Logger LOG;

  public void inject(Player player) {
    Channel ch = ((CraftPlayer) player).getHandle().connection.connection.channel;
    if (ch == null) {
      LOG.warning("Channel is null for " + player.getName());
      return;
    }

    final String name = HANDLER_PREFIX + player.getUniqueId();
    if (ch.pipeline().get(name) != null) {
      LOG.info("Already injected for " + player.getName());
      return;
    }

    // (One-time) list pipeline stages to confirm "packet_handler" exists
    LOG.info("Pipeline for " + player.getName() + ": " + ch.pipeline().names());

    ch.pipeline().addBefore("packet_handler", "bus-"+player.getUniqueId(), new ChannelDuplexHandler() {
      // short window to swallow any trailing break sound after we spoof/replace (avoid doubles)
      private long muteBreakSoundsUntilNanos = 0L;
      private boolean sendingSpoof = false; // re-entry guard for our own 2001

      @Override
      public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        // A) Physics/pop-off path: destroy block effect (often used for snow/torch/etc.)
        if (msg instanceof net.minecraft.network.protocol.game.ClientboundLevelEventPacket lev
            && lev.getType() == 2001) {

          if (sendingSpoof) { // let our own spoofed packet pass
            super.write(ctx, msg, promise);
            return;
          }

          // swallow original
          promise.trySuccess();

          // open a short mute window so a trailing block.*.break doesn't stack
          muteBreakSoundsUntilNanos = System.nanoTime() + java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(150);

          // Option 1: send your own 2001 as "stone" (particles+sound like stone)
          int data = net.minecraft.world.level.block.Block.getId(
              net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
          var fake = new net.minecraft.network.protocol.game.ClientboundLevelEventPacket(
              2001, lev.getPos(), data, false);

          sendingSpoof = true;
          ctx.writeAndFlush(fake, ctx.voidPromise()); // send immediately
          sendingSpoof = false;
          return;
        }

        // B) Player-mining path: separate sound packet with block.*.break
        if (msg instanceof net.minecraft.network.protocol.game.ClientboundSoundPacket sp
            && sp.getSource() == net.minecraft.sounds.SoundSource.BLOCKS) {

          var h = sp.getSound();
          if (h != null) {
            var key = net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.getKey(h.value());
            if (key != null) {
              String path = key.getPath(); // e.g. block.generic.break

              // swallow only the final break sound
              if (path.startsWith("block.") && path.endsWith(".break")) {
                if (System.nanoTime() < muteBreakSoundsUntilNanos) {
                  promise.trySuccess();
                  return;
                }

                // swallow vanilla break sound
                promise.trySuccess();

                // play your replacement (use your stored last break pos if you want precise center)
                org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
                  if (!player.isOnline()) return;
                  player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1f, 1f);
                });
                return;
              }
            }
          }
        }

        super.write(ctx, msg, promise);
      }
    });

    LOG.info("Injected handler for " + player.getName());
  }

  private boolean isBlockBreakSound(ClientboundSoundPacket sp) {
    // Only mute block-category sounds
    if (sp.getSource() != SoundSource.BLOCKS) {
      return false;
    }

    // Resolve the registry key of the sound
    Holder<SoundEvent> holder = sp.getSound();
    SoundEvent evt = holder.value();
    ResourceLocation key = BuiltInRegistries.SOUND_EVENT.getKey(evt);
    return isBlockBreakKey(key);
  }

  private boolean isBlockBreakKey(ResourceLocation key) {
    if (key == null) {
      return false;
    }
    // Typical keys look like: "minecraft:block.wood.break", "minecraft:block.stone.break", etc.
    // Filter all block break variants, or narrow to specific families if you want:
    String ns = key.getNamespace(); // "minecraft"
    String path = key.getPath();    // e.g. "block.wood.break"
    if (!"minecraft".equals(ns)) {
      return false; // ignore modded unless you want them
    }
    if (!path.startsWith("block.")) {
      return false;
    }
    return path.endsWith(".break");
    // If you only want WOOD break:
    // return "block.wood.break".equals(path);
  }

  public void eject(Player player) {
    Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
    String name = "bus" + player.getUniqueId();
    if (channel.pipeline().get(name) != null) {
      channel.pipeline().remove(name);
    }
  }
}

