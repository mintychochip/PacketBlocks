package org.aincraft.api;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class EntityMapper {

  private static final Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends Entity>> BUKKIT_TO_NMS = new HashMap<>();

  static {
    // Passive Mobs
    BUKKIT_TO_NMS.put(org.bukkit.entity.Cow.class, net.minecraft.world.entity.animal.Cow.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Sheep.class,
        net.minecraft.world.entity.animal.sheep.Sheep.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Pig.class, net.minecraft.world.entity.animal.Pig.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Chicken.class,
        net.minecraft.world.entity.animal.Chicken.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Villager.class,
        net.minecraft.world.entity.npc.Villager.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Horse.class,
        net.minecraft.world.entity.animal.horse.Horse.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Wolf.class,
        net.minecraft.world.entity.animal.wolf.Wolf.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Cat.class, net.minecraft.world.entity.animal.Cat.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Fox.class, net.minecraft.world.entity.animal.Fox.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Bat.class, net.minecraft.world.entity.ambient.Bat.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Zombie.class,
        net.minecraft.world.entity.monster.Zombie.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Skeleton.class,
        net.minecraft.world.entity.monster.Skeleton.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Creeper.class,
        net.minecraft.world.entity.monster.Creeper.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Spider.class,
        net.minecraft.world.entity.monster.Spider.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Witch.class,
        net.minecraft.world.entity.monster.Witch.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Enderman.class,
        net.minecraft.world.entity.monster.EnderMan.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Slime.class,
        net.minecraft.world.entity.monster.Slime.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.MagmaCube.class,
        net.minecraft.world.entity.monster.MagmaCube.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Wither.class,
        net.minecraft.world.entity.boss.wither.WitherBoss.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Item.class,
        net.minecraft.world.entity.item.ItemEntity.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Boat.class, net.minecraft.world.entity.vehicle.Boat.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Minecart.class,
        net.minecraft.world.entity.vehicle.AbstractMinecart.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Snowball.class,
        net.minecraft.world.entity.projectile.Snowball.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Arrow.class,
        net.minecraft.world.entity.projectile.Arrow.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.ExperienceOrb.class,
        net.minecraft.world.entity.ExperienceOrb.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.Painting.class,
        net.minecraft.world.entity.decoration.Painting.class);
    BUKKIT_TO_NMS.put(org.bukkit.entity.ArmorStand.class,
        net.minecraft.world.entity.decoration.ArmorStand.class);
  }

  @NotNull
  public static Class<? extends Entity> getNMSEntityClass(
      Class<? extends org.bukkit.entity.Entity> bukkitClass) throws IllegalArgumentException {
    Class<? extends Entity> nmsClass = BUKKIT_TO_NMS.get(bukkitClass);
    Preconditions.checkArgument(nmsClass != null);
    return
  }

  public static <T extends Entity> T create(EntityType type) {

  }
}
