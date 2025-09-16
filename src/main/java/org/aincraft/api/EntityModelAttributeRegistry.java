package org.aincraft.api;

import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.aincraft.api.EntityModelAttributes.EntityModelAttributeImpl;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class EntityModelAttributeRegistry {

  private EntityModelAttributeRegistry() {
    throw new UnsupportedOperationException("do not instantiate");
  }

  static final Map<String, EntityModelAttributeImpl<?>> REGISTERED_ATTRIBUTES = new HashMap<>();

  static {
    registerAttribute("transform", org.bukkit.util.Transformation.class);
    registerAttribute("translation", Vector3f.class);
    registerAttribute("scale", Vector3f.class);
    registerAttribute("left_rotation", Quaternionf.class);
    registerAttribute("right_rotation", Quaternionf.class);
    registerAttribute("position", Vector3f.class);
    registerAttribute("item_model", Key.class);
    registerAttribute("invisible", Boolean.class);
    registerAttribute("glowing", Boolean.class);
    registerAttribute("glow_color", Integer.class);
  }

  private static <T> void registerAttribute(String key, Class<T> clazz) {
    REGISTERED_ATTRIBUTES.put(key, new EntityModelAttributeImpl<>(key, clazz));
  }
}
