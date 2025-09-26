package org.aincraft;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import org.aincraft.EntityModelData.EntityModelAttribute;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class EntityModelAttributes {

  private EntityModelAttributes() {
    throw new UnsupportedOperationException("do not instantiate");
  }

  public static final EntityModelAttribute<Vector3f> TRANSLATION = attribute("translation");
  public static final EntityModelAttribute<Vector3f> SCALE = attribute("scale");
  public static final EntityModelAttribute<Quaternionf> LEFT_ROTATION = attribute("left_rotation");
  public static final EntityModelAttribute<Quaternionf> RIGHT_ROTATION = attribute(
      "right_rotation");
  public static final EntityModelAttribute<World> WORLD = attribute("world");
  public static final EntityModelAttribute<Vector3f> POSITION = attribute("position");
  public static final EntityModelAttribute<Key> ITEM_MODEL = attribute("item_model");
  public static final EntityModelAttribute<Boolean> INVISIBLE = attribute("invisible");
  public static final EntityModelAttribute<Boolean> GLOWING = attribute("glowing");
  public static final EntityModelAttribute<Integer> GLOW_COLOR_OVERRIDE = attribute("glow_color");
  public static final EntityModelAttribute<Integer> SLIME_SIZE = attribute("slime_size");
  public static final EntityModelAttribute<Float> SHULKER_PEEK = attribute("shulker_peek");

  @SuppressWarnings("unchecked")
  @NotNull
  static <T> EntityModelAttribute<T> attribute(String key) throws IllegalArgumentException {
    EntityModelAttributeImpl<?> attribute = EntityModelAttributeRegistry.REGISTERED_ATTRIBUTES.get(
        key);
    Preconditions.checkArgument(attribute != null);
    return (EntityModelAttributeImpl<T>) attribute;
  }

  public record EntityModelAttributeImpl<T>(String key, Class<T> clazz) implements
      EntityModelAttribute<T> {

  }
}
