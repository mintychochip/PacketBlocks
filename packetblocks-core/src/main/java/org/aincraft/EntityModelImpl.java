package org.aincraft;

import com.google.common.base.Preconditions;
import com.mojang.math.Transformation;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.key.Key;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Entity;
import org.aincraft.EntityModelAttributes.EntityModelAttributeImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class EntityModelImpl<T extends Entity> implements EntityModel {

  private static final Vector3f ZERO_VECTOR = new Vector3f(0.0f);

  private EntityModelData data;
  private final Set<Player> viewers = new HashSet<>();
  private final T delegate;

  public EntityModelImpl(EntityModelData data, T delegate) {
    this.delegate = delegate;
    setData(data);
  }

  public static EntityModel create(EntityType entityType, World world, Vector position) {
    EntityModelData meta = EntityModelDataImpl.create();
    Entity nmsEntity = EntityMapper.create(entityType, world);
    //TODO: throw an error here
    nmsEntity.setPos(position.getX(), position.getY(), position.getZ());
    return new EntityModelImpl<>(meta, nmsEntity);
  }

  @Override
  public boolean visible(Player player) {
    return viewers.contains(player);
  }

  @Override
  public void show(Player player) {
    if (!viewers.add(player)) {
      return;
    }
    if (player instanceof CraftPlayer craftPlayer) {
      ServerPlayer handle = craftPlayer.getHandle();
      List<Packet<? super ClientGamePacketListener>> packets = List.of(
          new ClientboundAddEntityPacket(delegate.getId(), delegate.getUUID(), delegate.getX(),
              delegate.getY(), delegate.getZ(), delegate.getXRot(), delegate.getYRot(),
              delegate.getType(), 0, delegate.getDeltaMovement(), delegate.getYHeadRot()),
          updatePacket()
      );
      ClientboundBundlePacket bundle = new ClientboundBundlePacket(packets);
      handle.connection.send(bundle);
    }
  }

  @Override
  public void hide(Player player) {
    if (viewers.remove(player) && player instanceof CraftPlayer craftPlayer) {
      ServerPlayer handle = craftPlayer.getHandle();
      ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(
          delegate.getId());
      handle.connection.send(packet);
    }
  }

  private static final int    UNITS_PER_BLOCK = 4096;           // 1/4096 block

  @Override
  public void move(Location to) {
    Vector tp = to.toVector();
    Vector delta = tp.subtract(new Vector(delegate.getX(), delegate.getY(), delegate.getZ()));
    Vector deltaUnits = delta.multiply(4096);

    for (int i = 0; i < 1; i++) {


      ClientboundMoveEntityPacket.Pos pkt =
          new ClientboundMoveEntityPacket.Pos(
              delegate.getId(), (short) deltaUnits.getX(), (short) deltaUnits.getY(), (short) deltaUnits.getZ(), delegate.onGround()
          );
      all(pkt);
    }
  }

  @Override
  public void teleport(Location location) {
  }

  @Override
  public Set<Player> viewers() {
    return viewers;
  }

  public void setData(EntityModelData data) {
    delegate.setInvisible(data.getAttribute(EntityModelAttributes.INVISIBLE, false));
    delegate.setGlowingTag(data.getAttribute(EntityModelAttributes.GLOWING, false));
    if (delegate instanceof Display display) {
      Vector3f scale = data.getAttribute(EntityModelAttributes.SCALE);
      Vector3f translation = data.getAttribute(EntityModelAttributes.TRANSLATION);
      Quaternionf leftRotation = data.getAttribute(EntityModelAttributes.LEFT_ROTATION);
      Quaternionf rightRotation = data.getAttribute(
          EntityModelAttributes.RIGHT_ROTATION);
      display.setTransformation(new Transformation(translation, leftRotation, scale,
          rightRotation));
      display.setGlowColorOverride(
          data.getAttribute(EntityModelAttributes.GLOW_COLOR_OVERRIDE, 0));
    }
    if (delegate instanceof ItemDisplay itemDisplay) {
      ItemStack itemStack = ItemStack.of(Material.PAPER);
      Key itemModel = data.getAttribute(EntityModelAttributes.ITEM_MODEL);
      itemStack.setData(DataComponentTypes.ITEM_MODEL, itemModel);
      itemDisplay.setItemStack(net.minecraft.world.item.ItemStack.fromBukkitCopy(itemStack));
    }
    if (delegate instanceof Shulker shulker) {
      float peek = data.getAttribute(EntityModelAttributes.SHULKER_PEEK);
      shulker.setPeek(peek);
    }
    Packet<? super ClientGamePacketListener> packet = updatePacket();
    viewers.forEach(viewer -> {
      if (viewer instanceof CraftPlayer craftPlayer) {
        ServerPlayer handle = craftPlayer.getHandle();
        handle.connection.send(packet);
      }
    });
    this.data = data;
  }

  @Override
  public EntityModelData getData() {
    return data;
  }

  protected Packet<? super ClientGamePacketListener> updatePacket() {
    SynchedEntityData data = delegate.getEntityData();
    return new ClientboundSetEntityDataPacket(delegate.getId(), data.packAll());
  }

  private void all(Packet<? extends ClientGamePacketListener> packet) {
    viewers.forEach(viewer -> {
      if (viewer instanceof CraftPlayer craftPlayer) {
        ServerPlayer handle = craftPlayer.getHandle();
        handle.connection.send(packet);
      }
    });
  }

  record EntityModelDataImpl(Map<String, Object> attributes) implements EntityModelData {

    static EntityModelData create() throws IllegalStateException {
      List<World> worlds = Bukkit.getWorlds();
      Preconditions.checkState(!worlds.isEmpty());
      EntityModelData data = new EntityModelDataImpl(new HashMap<>());
      data.setAttribute(EntityModelAttributes.TRANSLATION, new Vector3f(0.5f));
      data.setAttribute(EntityModelAttributes.SCALE, new Vector3f(1.0001f));
      data.setAttribute(EntityModelAttributes.LEFT_ROTATION,
          new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f));
      data.setAttribute(EntityModelAttributes.RIGHT_ROTATION,
          new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f));
      data.setAttribute(EntityModelAttributes.WORLD, worlds.getFirst());
      data.setAttribute(EntityModelAttributes.POSITION, ZERO_VECTOR);
      data.setAttribute(EntityModelAttributes.INVISIBLE, false);
      data.setAttribute(EntityModelAttributes.GLOWING, false);
      data.setAttribute(EntityModelAttributes.GLOW_COLOR_OVERRIDE, 0);
      data.setAttribute(EntityModelAttributes.WORLD, worlds.getFirst());
      data.setAttribute(EntityModelAttributes.ITEM_MODEL, Key.key("minecraft:stone"));
      data.setAttribute(EntityModelAttributes.SLIME_SIZE, 0);
      data.setAttribute(EntityModelAttributes.SHULKER_PEEK, 0.0f);
      return data;
    }


    @Override
    public <T> @NotNull T getAttribute(EntityModelAttribute<T> attribute, T def) {
      T t = getAttribute(attribute);
      return t != null ? t : def;
    }

    @Override
    public <T> @Nullable T getAttribute(EntityModelAttribute<T> attribute) {
      if (attribute instanceof EntityModelAttributeImpl<T>(String key, Class<T> clazz)) {
        Object object = attributes.get(key);
        if (object != null) {
          return clazz.cast(object);
        }
      }
      return null;
    }

    @Override
    public <T> void setAttribute(EntityModelAttribute<T> attribute, T value) {
      if (attribute instanceof EntityModelAttributeImpl<T> cast) {
        attributes.put(cast.key(), value);
      }
    }
  }

}