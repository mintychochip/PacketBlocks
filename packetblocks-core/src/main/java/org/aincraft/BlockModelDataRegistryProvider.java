package org.aincraft;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import java.util.List;
import org.aincraft.BlockModelData.Builder;
import org.aincraft.BlockModelDataImpl.BuilderImpl;
import org.aincraft.config.YamlConfiguration;
import org.aincraft.registry.Registry;
import org.aincraft.registry.RegistryAccess;
import org.aincraft.registry.RegistryAccessKeys;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class BlockModelDataRegistryProvider implements Provider<Registry<BlockModelData>> {

  private static final String DEFAULT_ITEM_MODEL = "minecraft:stone";
  private static final Vector3f DEFAULT_TRANSLATION = new Vector3f(0.5f);
  private static final Vector3f DEFAULT_SCALE = new Vector3f(1.001f);
  private static final Quaternionf DEFAULT_ROTATION = new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);

  private final Plugin plugin;

  @Inject
  BlockModelDataRegistryProvider(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public Registry<BlockModelData> get() {
    YamlConfiguration config = YamlConfiguration.single(plugin, "config.yml");
    Registry<BlockModelData> registry = Registry.simple();
    for (String key : config.getKeys(false)) {
      ConfigurationSection blockModelSection = config.getConfigurationSection(key);
      if (blockModelSection == null) {
        continue;
      }
      Builder builder = new BuilderImpl(NamespacedKey.fromString(key));
      if (blockModelSection.contains("item-model")) {
        builder.itemModel(
            NamespacedKey.fromString(blockModelSection.getString("item-model", DEFAULT_ITEM_MODEL)));
      }
      if (blockModelSection.contains("translation")) {
        builder.translation(vector3f(blockModelSection.getFloatList("translation"), DEFAULT_TRANSLATION));
      }
      if (blockModelSection.contains("scale")) {
        builder.scale(vector3f(blockModelSection.getFloatList("scale"), DEFAULT_SCALE));
      }
      if (blockModelSection.contains("left-rotation")) {
        builder.leftRotation(quaternionf(blockModelSection.getFloatList("left-rotation"), DEFAULT_ROTATION));
      }
      if (blockModelSection.contains("right-rotation")) {
        builder.leftRotation(quaternionf(blockModelSection.getFloatList("right-rotation"), DEFAULT_ROTATION));
      }
      registry.register(builder.build());
    }
    return registry;
  }

  private static Vector3f vector3f(List<Float> floats, Vector3f def)
      throws IllegalArgumentException {
    if (floats.isEmpty()) {
      return def;
    }
    Preconditions.checkArgument(floats.size() >= 3);
    return new Vector3f(floats.get(0), floats.get(1), floats.get(2));
  }

  private static Quaternionf quaternionf(List<Float> floats, Quaternionf def)
      throws IllegalArgumentException {
    if (floats.isEmpty()) {
      return def;
    }
    Preconditions.checkArgument(floats.size() >= 3);
    return new Quaternionf(floats.get(0), floats.get(1), floats.get(2), floats.get(3));
  }
}
