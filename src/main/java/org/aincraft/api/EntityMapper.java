package org.aincraft.api;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.EntitySpawnReason;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityMapper {

  private static final Map<EntityType, net.minecraft.world.entity.EntityType<?>> BUKKIT_TO_NMS_TYPE = new HashMap<>();

  static {
    BUKKIT_TO_NMS_TYPE.put(EntityType.COW, net.minecraft.world.entity.EntityType.COW);
    BUKKIT_TO_NMS_TYPE.put(EntityType.SHEEP, net.minecraft.world.entity.EntityType.SHEEP);
    BUKKIT_TO_NMS_TYPE.put(EntityType.PIG, net.minecraft.world.entity.EntityType.PIG);
    BUKKIT_TO_NMS_TYPE.put(EntityType.CHICKEN, net.minecraft.world.entity.EntityType.CHICKEN);
    BUKKIT_TO_NMS_TYPE.put(EntityType.VILLAGER, net.minecraft.world.entity.EntityType.VILLAGER);
    BUKKIT_TO_NMS_TYPE.put(EntityType.HORSE, net.minecraft.world.entity.EntityType.HORSE);
    BUKKIT_TO_NMS_TYPE.put(EntityType.WOLF, net.minecraft.world.entity.EntityType.WOLF);
    BUKKIT_TO_NMS_TYPE.put(EntityType.CAT, net.minecraft.world.entity.EntityType.CAT);
    BUKKIT_TO_NMS_TYPE.put(EntityType.FOX, net.minecraft.world.entity.EntityType.FOX);
    BUKKIT_TO_NMS_TYPE.put(EntityType.BAT, net.minecraft.world.entity.EntityType.BAT);

    BUKKIT_TO_NMS_TYPE.put(EntityType.SHULKER,net.minecraft.world.entity.EntityType.SHULKER);
    BUKKIT_TO_NMS_TYPE.put(EntityType.ZOMBIE, net.minecraft.world.entity.EntityType.ZOMBIE);
    BUKKIT_TO_NMS_TYPE.put(EntityType.SKELETON, net.minecraft.world.entity.EntityType.SKELETON);
    BUKKIT_TO_NMS_TYPE.put(EntityType.CREEPER, net.minecraft.world.entity.EntityType.CREEPER);
    BUKKIT_TO_NMS_TYPE.put(EntityType.SPIDER, net.minecraft.world.entity.EntityType.SPIDER);
    BUKKIT_TO_NMS_TYPE.put(EntityType.WITCH, net.minecraft.world.entity.EntityType.WITCH);
    BUKKIT_TO_NMS_TYPE.put(EntityType.ENDERMAN, net.minecraft.world.entity.EntityType.ENDERMAN);
    BUKKIT_TO_NMS_TYPE.put(EntityType.SLIME, net.minecraft.world.entity.EntityType.SLIME);
    BUKKIT_TO_NMS_TYPE.put(EntityType.MAGMA_CUBE, net.minecraft.world.entity.EntityType.MAGMA_CUBE);
    BUKKIT_TO_NMS_TYPE.put(EntityType.WITHER, net.minecraft.world.entity.EntityType.WITHER);

    BUKKIT_TO_NMS_TYPE.put(EntityType.ITEM, net.minecraft.world.entity.EntityType.ITEM);
    BUKKIT_TO_NMS_TYPE.put(EntityType.MINECART, net.minecraft.world.entity.EntityType.MINECART);
    BUKKIT_TO_NMS_TYPE.put(EntityType.SNOWBALL, net.minecraft.world.entity.EntityType.SNOWBALL);
    BUKKIT_TO_NMS_TYPE.put(EntityType.ARROW, net.minecraft.world.entity.EntityType.ARROW);
    BUKKIT_TO_NMS_TYPE.put(EntityType.EXPERIENCE_ORB,
        net.minecraft.world.entity.EntityType.EXPERIENCE_ORB);
    BUKKIT_TO_NMS_TYPE.put(EntityType.PAINTING, net.minecraft.world.entity.EntityType.PAINTING);
    BUKKIT_TO_NMS_TYPE.put(EntityType.ARMOR_STAND,
        net.minecraft.world.entity.EntityType.ARMOR_STAND);
    BUKKIT_TO_NMS_TYPE.put(EntityType.ITEM_DISPLAY,net.minecraft.world.entity.EntityType.ITEM_DISPLAY);
  }

  @Nullable
  public static Entity create(EntityType type, World world) throws IllegalArgumentException {
    net.minecraft.world.entity.EntityType<?> entityType = BUKKIT_TO_NMS_TYPE.get(type);
    Preconditions.checkArgument(entityType != null);
    CraftWorld craftWorld = (CraftWorld) world;
    return entityType.create(craftWorld.getHandle(), EntitySpawnReason.COMMAND);
  }
}
