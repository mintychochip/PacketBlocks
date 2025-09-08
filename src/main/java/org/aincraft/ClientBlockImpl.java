package org.aincraft;

import com.mojang.math.Transformation;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.aincraft.api.ClientBlock;
import org.aincraft.api.ClientBlockData;
import org.aincraft.domain.ClientBlockDataImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class ClientBlockImpl implements ClientBlock {

  private ClientBlockData blockData;
  private final World world;
  private final Vector position;
  private final Set<Player> viewers = new HashSet<>();
  private final ItemDisplay itemDisplay;

  public ClientBlockImpl(ClientBlockData blockData, World world, Vector position,
      ItemDisplay itemDisplay) {
    this.blockData = blockData;
    this.world = world;
    this.position = position;
    this.itemDisplay = itemDisplay;
  }

  public static ClientBlockImpl create(ClientBlockData blockData, World world) {
    return create(blockData, world, new Vector());
  }

  public static ClientBlockImpl create(ClientBlockData blockData, World world, Vector position) {
    CraftWorld craftWorld = (CraftWorld) world;
    ItemDisplay display = new ItemDisplay(EntityType.ITEM_DISPLAY, craftWorld.getHandle());
    display.setPos(new Vec3(position.getX(), position.getY(), position.getZ()));
    ItemStack bukkitStack = ItemStack.of(Material.PAPER);
    bukkitStack.setData(DataComponentTypes.ITEM_MODEL, blockData.itemModel());
    net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(bukkitStack);
    display.setItemStack(itemStack);
    display.setTransformation(
        new Transformation(blockData.translation(), blockData.leftRotation(), blockData.scale(),
            blockData.rightRotation()));
    Brightness brightness = ClientBlockDataImpl.asNMSBrightness(blockData.blockLight(),
        blockData.skyLight());
    display.setBrightnessOverride(brightness);
    display.setViewRange(blockData.range());
    display.setGlowColorOverride(1);
    return new ClientBlockImpl(blockData, world, position, display);
  }

  @Override
  public ClientBlockData getBlockData() {
    return blockData;
  }

  @Override
  public Vector getPosition() {
    return position;
  }

  @Override
  public void setBlockData(ClientBlockData blockData) {
    this.blockData = blockData;
    itemDisplay.setPos(new Vec3(position.getX(), position.getY(), position.getZ()));
    ItemStack bukkitStack = ItemStack.of(Material.PAPER);
    bukkitStack.setData(DataComponentTypes.ITEM_MODEL, blockData.itemModel());
    net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(bukkitStack);
    itemDisplay.setItemStack(itemStack);
    itemDisplay.setTransformation(
        new Transformation(blockData.translation(), blockData.leftRotation(), blockData.scale(),
            blockData.rightRotation()));
    Brightness brightness = ClientBlockDataImpl.asNMSBrightness(blockData.blockLight(),
        blockData.skyLight());
    itemDisplay.setBrightnessOverride(brightness);
    itemDisplay.setViewRange(blockData.range());
  }

  @Override
  public boolean visibleTo(Player player) {
    return viewers.add(player);
  }

  @Override
  public void show(Player player) {
    if (!(player instanceof CraftPlayer craftPlayer)) {
      return;
    }
    ServerPlayer serverPlayer = craftPlayer.getHandle();
    if (viewers.contains(player)) {
      return;
    }
    viewers.add(player);
    ClientboundAddEntityPacket packet = new ClientboundAddEntityPacket(
        itemDisplay.getId(), itemDisplay.getUUID(), itemDisplay.getX(),
        itemDisplay.getY(), itemDisplay.getZ(), itemDisplay.getXRot(),
        itemDisplay.getYRot(), EntityType.ITEM_DISPLAY, 0, itemDisplay.getDeltaMovement(),
        itemDisplay.getYHeadRot());
    serverPlayer.connection.send(packet);
    SynchedEntityData data = itemDisplay.getEntityData();
    ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(
        itemDisplay.getId(), data.packAll());
    serverPlayer.connection.send(dataPacket);
  }

  @Override
  public void hide(Player player) {
    if (!(player instanceof CraftPlayer craftPlayer)) {
      return;
    }
    ServerPlayer serverPlayer = craftPlayer.getHandle();
    if (!viewers.contains(player)) {
      return;
    }
    viewers.remove(player);
    ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(
        itemDisplay.getId());
    serverPlayer.connection.send(packet);
  }

  @Override
  public void teleport(Vector position) {
    Vec3 pos = new Vec3(position.getX(), position.getY(), position.getZ());
    itemDisplay.setPos(pos);
//    PositionMoveRotation moveRotation = new PositionMoveRotation(pos
//        , itemDisplay.getDeltaMovement(),
//        itemDisplay.getYRot(), itemDisplay.getXRot());
//    ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket(
//        itemDisplay.getId(), moveRotation, Set.of(
//        Relative.X, Relative.Y, Relative.Z, Relative.X_ROT, Relative.Y_ROT), false);
//    viewers.forEach(viewer -> viewer.connection.send(packet));
  }

  @Override
  public Set<Player> viewers() {
    return viewers;
  }
}
