package org.aincraft;

import com.mojang.math.Transformation;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.key.Key;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ClientBlockImpl implements ClientBlock {

  private static final Key DEFAULT_ITEM_MODEL;
  private static final Vec3 DEFAULT_POSITION;
  private static final Transformation DEFAULT_TRANSFORMATION;
  private static final float DEFAULT_RANGE;

  static {
    float scale = 1.0001f;
    float translation = (scale / 2.0f) - 0.5f;
    DEFAULT_ITEM_MODEL = Key.key("minecraft:stone");
    DEFAULT_POSITION = new Vec3(translation, translation, translation);
    DEFAULT_TRANSFORMATION = new Transformation(new Vector3f(0, 0, 0), new Quaternionf(0, 0, 0, 1),
        new Vector3f(scale, scale, scale),
        new Quaternionf(0, 0, 0, 1));
    DEFAULT_RANGE = 32.0f;
  }

  private final Key itemModel;
  private final ServerLevel level;
  private final Vec3 position;
  @Nullable
  private final Brightness brightness;
  private final Transformation transformation;
  private final Set<ServerPlayer> viewers;
  private final ItemDisplay itemDisplay;
  private final Plugin plugin;

  public ClientBlockImpl(Key itemModel, ServerLevel level, Vec3 position,
      @Nullable Brightness brightness, Transformation transformation, Set<ServerPlayer> viewers,
      ItemDisplay itemDisplay, Plugin plugin) {
    this.itemModel = itemModel;
    this.level = level;
    this.position = position;
    this.brightness = brightness;
    this.transformation = transformation;
    this.viewers = viewers;
    this.itemDisplay = itemDisplay;
    this.plugin = plugin;
  }

  public static ClientBlock.Builder builder(World world, Plugin plugin) {
    CraftWorld craftWorld = (CraftWorld) world;
    return new Builder(craftWorld.getHandle(), DEFAULT_ITEM_MODEL, DEFAULT_POSITION
        , DEFAULT_TRANSFORMATION, DEFAULT_RANGE, new HashSet<>(), plugin, null);
  }

  public static ClientBlock create(World world, Vector position, Plugin plugin) {
    CraftWorld craftWorld = (CraftWorld) world;
    Builder builder = new Builder(craftWorld.getHandle(), Key.key("minecraft:stone"),
        new Vec3(position.getX(), position.getY(), position.getZ()), new Transformation(
        new Vector3f(0.5f, 0.5f, 0.5f),
        new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f),
        new Vector3f(1.0f, 1.0f, 1.0f),
        new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f)
    ), 1.0f, new HashSet<>(), plugin, null);
    return builder.build();
  }

  @Override
  public Key getModel() {
    return itemModel;
  }

  @Override
  public boolean visibleTo(Player player) {
    if (player instanceof ServerPlayer serverPlayer) {
      return viewers.add(serverPlayer);
    }
    return false;
  }

  @Override
  public void show(Player player) {
    if (!(player instanceof CraftPlayer craftPlayer)) {
      return;
    }
    ServerPlayer serverPlayer = craftPlayer.getHandle();
    if (viewers.contains(serverPlayer)) {
      return;
    }
    viewers.add(serverPlayer);
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
    if (!viewers.contains(serverPlayer)) {
      return;
    }
    viewers.remove(serverPlayer);
    ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(
        itemDisplay.getId());
    serverPlayer.connection.send(packet);
  }

  @Override
  public void teleport(Vector position) {

  }

  private static final class Builder implements ClientBlock.Builder {

    private Key itemModel;
    private ServerLevel level;
    private Vec3 position;
    @Nullable
    private Brightness brightness;
    private Transformation transformation;
    private float range;
    private Set<ServerPlayer> viewers;
    private final Plugin plugin;

    public Builder(ServerLevel level, Key itemModel, Vec3 position, Transformation transformation,
        float range, Set<ServerPlayer> viewers, Plugin plugin, @Nullable Brightness brightness) {
      this.itemModel = itemModel;
      this.level = level;
      this.position = position;
      this.brightness = brightness;
      this.transformation = transformation;
      this.range = range;
      this.viewers = viewers;
      this.plugin = plugin;
    }

    @Override
    public ClientBlock.Builder setItemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    @Override
    public ClientBlock.Builder setLocation(Location location, boolean aligned) {
      World world = location.getWorld();
      CraftWorld craftWorld = (CraftWorld) world;
      this.level = craftWorld.getHandle();
      if (aligned) {
        this.position = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        this.transformation = new Transformation(
            new Vector3f(0.5f, 0.5f, 0.5f).sub(transformation.getTranslation()),
            transformation.getLeftRotation(),
            transformation.getScale(),
            transformation.getRightRotation());
        return this;
      }
      this.position = new Vec3(location.getX(), location.getY(), location.getZ());
      return this;
    }

    @Override
    public ClientBlock.Builder setTransformation(org.bukkit.util.Transformation transformation) {
      this.transformation = asMojangTransformation(transformation);
      return this;
    }

    @Override
    public ClientBlock.Builder addViewer(Player player) {

      return this;
    }

    @Override
    public ClientBlock.Builder setViewRange(float range) {
      this.range = range;
      return this;
    }

    @Override
    public ClientBlock build() {
      ItemDisplay display = new ItemDisplay(EntityType.ITEM_DISPLAY, level);
      display.setPos(position);
      display.setBrightnessOverride(brightness);
      ItemStack bukkitStack = ItemStack.of(Material.PAPER);
      bukkitStack.setData(DataComponentTypes.ITEM_MODEL, itemModel);
      net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(bukkitStack);
      display.setViewRange(range);
      display.setItemStack(itemStack);
      display.setTransformation(transformation);
      return new ClientBlockImpl(itemModel, level, position, brightness, transformation, viewers,
          display, plugin);
    }

    @NotNull
    private static Transformation asMojangTransformation(
        @NotNull org.bukkit.util.Transformation transformation) {
      return new Transformation(transformation.getTranslation(), transformation.getLeftRotation(),
          transformation.getScale(), transformation.getRightRotation());
    }
  }

  private static Vec3 toVec3(Vector vector) {
    return new Vec3(vector.getX(), vector.getY(), vector.getZ());
  }
}
