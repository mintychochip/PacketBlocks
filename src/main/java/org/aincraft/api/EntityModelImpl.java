package org.aincraft.api;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3;

public class EntityModelImpl<T extends Entity> implements EntityModel<T> {

  private final ServerLevel level;
  private final Vec3 position;
  private final Set<ServerPlayer> viewers = new HashSet<>();
  private final T delegate;

  public EntityModelImpl(ServerLevel level, Vec3 position, T delegate) {
    this.level = level;
    this.position = position;
    this.delegate = delegate;
    delegate.setPos(position);
    delegate.setLevel(level);

  }

  @Override
  public Vec3 position() {
    return position;
  }

  @Override
  public ServerLevel world() {
    return level;
  }

  @Override
  public boolean visible(ServerPlayer player) {
    return viewers.contains(player);
  }

  @Override
  public void show(ServerPlayer player) {
    if (!viewers.add(player)) {
      return;
    }
    List<Packet<? super ClientGamePacketListener>> packets = List.of(
        new ClientboundAddEntityPacket(delegate.getId(), delegate.getUUID(), delegate.getX(),
            delegate.getY(), delegate.getZ(), delegate.getXRot(),
            delegate.getYRot(), delegate.getType(), 0, delegate.getDeltaMovement(),
            delegate.getYHeadRot()),
        updatePacket()
    );
    ClientboundBundlePacket bundle = new ClientboundBundlePacket(packets);
    player.connection.send(bundle);
  }

  @Override
  public void hide(ServerPlayer player) {
    if (!viewers.remove(player)) {
      return;
    }

    ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(delegate.getId());
    player.connection.send(remove);
  }

  @Override
  public void teleport(Vec3 position) {
    // move the server-side entity
    delegate.teleportTo(position.x, position.y, position.z);

    // build the PositionMoveRotation
    PositionMoveRotation change = new PositionMoveRotation(
        position,
        Vec3.ZERO,
        delegate.getYRot(),
        delegate.getXRot()
    );

    // absolute teleport, no relatives
    Set<Relative> relatives = java.util.EnumSet.noneOf(Relative.class);

    // create the teleport packet
    ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket(
        delegate.getId(),
        change,
        relatives,
        delegate.onGround()
    );

    // send to all current viewers
    all(packet);
  }


  @Override
  public Set<ServerPlayer> viewers() {
    return viewers;
  }

  @Override
  public T delegate() {
    return delegate;
  }

  @Override
  public void setGlowing(boolean glow) {
    delegate.setGlowingTag(glow);
    all(updatePacket());
  }

  @Override
  public void setInvisible(boolean invisible) {
    delegate.setInvisible(invisible);
    all(updatePacket());
  }

  @Override
  public void push() {

  }

  protected void all(Packet<?> packet) {
    for (ServerPlayer viewer : viewers) {
      viewer.connection.send(packet);
    }
  }

  protected Packet<? super ClientGamePacketListener> updatePacket() {
    return new ClientboundSetEntityDataPacket(delegate.getId(), delegate.getEntityData().packAll());
  }
}
