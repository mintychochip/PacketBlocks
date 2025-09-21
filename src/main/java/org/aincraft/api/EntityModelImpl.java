package org.aincraft.api;

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
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Entity;
import org.aincraft.api.EntityModelAttributes.EntityModelAttributeImpl;
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

class EntityModelImpl<T extends Entity> implements EntityModel {

  private static final Vector3f ZERO_VECTOR = new Vector3f(0.0f);

  protected EntityModelMeta meta;
  protected final Set<Player> viewers = new HashSet<>();
  protected final T delegate;

  public EntityModelImpl(EntityModelMeta meta, T delegate) {
    this.delegate = delegate;
    setMeta(meta);
  }

  public static EntityModel create(EntityType entityType, World world, Vector position) {
    EntityModelMeta meta = EntityModelMetaImpl.create();
    Entity entity = EntityMapper.create(entityType, world);
    entity.setPos(position.getX(), position.getY(), position.getZ());
    return new EntityModelImpl<>(meta, entity);
  }

  @Override
  public boolean isVisible(Player player) {
    return viewers.contains(player);
  }

  @Override
  public void showTo(Player player) {
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
  public void hideFrom(Player player) {
    if (viewers.remove(player) && player instanceof CraftPlayer craftPlayer) {
      ServerPlayer handle = craftPlayer.getHandle();
      ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(
          delegate.getId());
      handle.connection.send(packet);
    }
  }

  @Override
  public void teleport(Location location) {

  }

  @Override
  public Set<Player> getViewers() {
    return viewers;
  }

  @Override
  public void setMeta(EntityModelMeta meta) {
    delegate.setInvisible(meta.getAttribute(EntityModelAttributes.INVISIBLE, false));
    delegate.setGlowingTag(meta.getAttribute(EntityModelAttributes.GLOWING, false));
    if (delegate instanceof Display display) {
      Vector3f scale = meta.getAttribute(EntityModelAttributes.SCALE);
      Vector3f translation = meta.getAttribute(EntityModelAttributes.TRANSLATION);
      Quaternionf leftRotation = meta.getAttribute(EntityModelAttributes.LEFT_ROTATION);
      Quaternionf rightRotation = meta.getAttribute(
          EntityModelAttributes.RIGHT_ROTATION);
      display.setTransformation(new Transformation(translation, leftRotation, scale,
          rightRotation));
      display.setGlowColorOverride(
          meta.getAttribute(EntityModelAttributes.GLOW_COLOR_OVERRIDE, 0));
    }
    if (delegate instanceof ItemDisplay itemDisplay) {
      ItemStack itemStack = ItemStack.of(Material.PAPER);
      Key itemModel = meta.getAttribute(EntityModelAttributes.ITEM_MODEL);
      itemStack.setData(DataComponentTypes.ITEM_MODEL, itemModel);
      itemDisplay.setItemStack(net.minecraft.world.item.ItemStack.fromBukkitCopy(itemStack));
    }
    if (delegate instanceof Shulker shulker) {
      float peek = meta.getAttribute(EntityModelAttributes.SHULKER_PEEK);
      shulker.setPeek(peek);
    }
    Packet<? super ClientGamePacketListener> packet = updatePacket();
    viewers.forEach(viewer -> {
      if (viewer instanceof CraftPlayer craftPlayer) {
        ServerPlayer handle = craftPlayer.getHandle();
        handle.connection.send(packet);
      }
    });
    this.meta = meta;
  }

  @Override
  public EntityModelMeta getMeta() {
    return meta;
  }

  protected Packet<? super ClientGamePacketListener> updatePacket() {
    SynchedEntityData data = delegate.getEntityData();
    return new ClientboundSetEntityDataPacket(delegate.getId(), data.packAll());
  }

  record EntityModelMetaImpl(Map<String, Object> attributes) implements EntityModelMeta {

    static EntityModelMeta create() throws IllegalStateException {
      List<World> worlds = Bukkit.getWorlds();
      Preconditions.checkState(!worlds.isEmpty());
      EntityModelMeta meta = new EntityModelMetaImpl(new HashMap<>());
      meta.setAttribute(EntityModelAttributes.TRANSLATION, new Vector3f(0.5f));
      meta.setAttribute(EntityModelAttributes.SCALE, new Vector3f(1.0001f));
      meta.setAttribute(EntityModelAttributes.LEFT_ROTATION,
          new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f));
      meta.setAttribute(EntityModelAttributes.RIGHT_ROTATION,
          new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f));
      meta.setAttribute(EntityModelAttributes.WORLD, worlds.getFirst());
      meta.setAttribute(EntityModelAttributes.POSITION, ZERO_VECTOR);
      meta.setAttribute(EntityModelAttributes.INVISIBLE, false);
      meta.setAttribute(EntityModelAttributes.GLOWING, false);
      meta.setAttribute(EntityModelAttributes.GLOW_COLOR_OVERRIDE, 0);
      meta.setAttribute(EntityModelAttributes.WORLD, worlds.getFirst());
      meta.setAttribute(EntityModelAttributes.ITEM_MODEL, Key.key("minecraft:stone"));
      meta.setAttribute(EntityModelAttributes.SLIME_SIZE, 0);
      meta.setAttribute(EntityModelAttributes.SHULKER_PEEK, 0.0f);
      return meta;
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