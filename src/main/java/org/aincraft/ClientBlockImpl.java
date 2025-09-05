package org.aincraft;

import com.mojang.math.Transformation;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.aincraft.ClientBlock.Builder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ClientBlockImpl implements ClientBlock {

  private final Key itemModel;
  private final Set<Player> playersVisible;
  private Location blockLocation;
  private final ItemDisplay itemDisplay;

  private ClientBlockImpl(Key itemModel, Set<Player> playersVisible, Location blockLocation,
      ItemDisplay itemDisplay) {
    this.itemModel = itemModel;
    this.playersVisible = playersVisible;
    this.blockLocation = blockLocation;
    this.itemDisplay = itemDisplay;
  }

  public static ClientBlock create(Key itemModel, Location blockLocation,
      net.minecraft.server.level.ServerLevel level) {
    ItemDisplay display = new ItemDisplay(EntityType.ITEM_DISPLAY, level);
    display.setPos(blockLocation.getBlockX(), blockLocation.getBlockY(), blockLocation.getBlockZ());
    ItemStack bukkitItemStack = ItemStack.of(Material.PAPER);
    bukkitItemStack.setData(DataComponentTypes.ITEM_MODEL, itemModel);
    net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(bukkitItemStack);
    display.setItemStack(itemStack);
    display.setBrightnessOverride(null);
    display.setBoundingBox(new AABB(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f));
    display.setTransformation(new Transformation(
        new Vector3f(0.5f, 0.5f, 0.5f),
        new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f),
        new Vector3f(1.0f, 1.0f, 1.0f),
        new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f)
    ));
    return new ClientBlockImpl(itemModel, new HashSet<>(), blockLocation, display);
  }

  @Override
  public Key getModel() {
    return itemModel;
  }

  @Override
  public boolean visibleTo(Player player) {
    return playersVisible.add(player);
  }

  @Override
  public void show(Player player) {
    if (playersVisible.contains(player)) {
      return;
    }
    CraftPlayer craftPlayer = (CraftPlayer) player;
    ServerPlayer serverPlayer = craftPlayer.getHandle();
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
  public void move(Location location) {
  }

  @Override
  public Location getBlockLocation() {
    return blockLocation;
  }

  @Override
  public ItemDisplay getDisplay() {
    return itemDisplay;
  }

  private static final class Builder implements ClientBlock.Builder {

    private Key itemModel;
    private ServerLevel level;
    private Vec3 position;
    @Nullable
    private Brightness brightness;
    private Transformation transformation;
    private Set<Entity> viewers;

    public Builder(Key itemModel, ServerLevel level, Vec3 position, @Nullable Brightness brightness,
        Transformation transformation,
        Set<Entity> viewers) {
      this.itemModel = itemModel;
      this.level = level;
      this.position = position;
      this.brightness = brightness;
      this.transformation = transformation;
      this.viewers = viewers;
    }

    @Override
    public ClientBlock.Builder setItemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    @Override
    public ClientBlock.Builder setLocation(Location location) {
      World world = location.getWorld();
      CraftWorld craftWorld = (CraftWorld) world;
      this.level = craftWorld.getHandle();
      this.position = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
      return this;
    }

    @Override
    public ClientBlock.Builder setTransformation(org.bukkit.util.Transformation transformation) {
      this.transformation = asMojangTransformation(transformation);
      return this;
    }

    @Override
    public ClientBlock.Builder addViewer(Entity viewer) {
      if (viewers == null) {
        return this;
      }
      viewers.add(viewer);
      return this;
    }

    @Override
    public ClientBlock build() {
      ItemDisplay display = new ItemDisplay(EntityType.ITEM_DISPLAY, level);
      display.setPos(position);
      display.setBrightnessOverride(brightness);
      display.setItemStack();
      display.setTransformation(transformation);
      return new ClientBlockImpl();
    }

    @NotNull
    private static Transformation asMojangTransformation(
        @NotNull org.bukkit.util.Transformation transformation) {
      return new Transformation(transformation.getTranslation(), transformation.getLeftRotation(),
          transformation.getScale(), transformation.getRightRotation());
    }
  }
}
