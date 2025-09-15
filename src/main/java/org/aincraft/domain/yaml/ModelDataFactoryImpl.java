package org.aincraft.domain.yaml;

import com.google.common.base.Preconditions;
import java.util.List;
import org.aincraft.api.ModelData;
import org.aincraft.api.ModelData.ModelDataBuilder;
import org.aincraft.config.ConfigurationFactory;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class ModelDataFactoryImpl implements ConfigurationFactory<ModelData> {

  private static final ModelData DEFAULT = ModelData.builder()
      .itemModel(Material.STONE.key())
      .translation(new Vector3f(0.5f))
      .scale(new Vector3f(1.001f))
      .rotation(new Quaternionf(0.0f,0.0f,0.0f,1.0f))
      .range(32.0f)
      .build();

  @Override
  public @NotNull ModelData create(@Nullable ConfigurationSection section)
      throws IllegalArgumentException {
    if (section == null) {
      return DEFAULT;
    }

    ModelDataBuilder builder = DEFAULT.toBuilder();

    if (section.isString("item-model")) {
      builder.itemModel(NamespacedKey.fromString(section.getString("item-model")));
    }
    if (section.contains("translation")) {
      builder.translation(vector3f(section.getFloatList("translation")));
    }
    if (section.contains("rotation")) {
      builder.rotation(quaternionf(section.getFloatList("rotation")));
    }
    if (section.contains("scale")) {
      builder.scale(vector3f(section.getFloatList("scale")));
    }
    if (section.contains("range")) {
      builder.range((float) section.getDouble("range"));
    }
    return builder.build();
  }

  private Vector3f vector3f(List<Float> list) throws IllegalArgumentException {
    Preconditions.checkArgument(list.size() == 3);
    Float x = list.get(0);
    Float y = list.get(1);
    Float z = list.get(2);
    return new Vector3f(x,y,z);
  }

  private Quaternionf quaternionf(List<Float> list) throws IllegalArgumentException {
    Preconditions.checkArgument(list.size() == 4);
    Float x = list.get(0);
    Float y = list.get(1);
    Float z = list.get(2);
    Float w = list.get(3);
    return new Quaternionf(x,y,z,w);
  }
}
