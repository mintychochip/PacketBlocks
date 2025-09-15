package org.aincraft.api;

import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display.BlockDisplay;
import net.minecraft.world.entity.Display.ItemDisplay;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public interface EntityModel<T extends Entity> {

  Vec3 position();

  ServerLevel world();

  boolean visible(ServerPlayer player);

  void show(ServerPlayer player);

  void hide(ServerPlayer player);

  void teleport(Vec3 position);

  Set<ServerPlayer> viewers();

  T delegate();

  void setGlowing(boolean glow);

  void setInvisible(boolean invisible);

  void push();

  interface ItemDisplayModel extends EntityModel<ItemDisplay> {

    ItemStack itemStack();

    void itemStack(ItemStack itemStack);
  }

  interface BlockDisplayModel extends EntityModel<BlockDisplay> {

  }
}
