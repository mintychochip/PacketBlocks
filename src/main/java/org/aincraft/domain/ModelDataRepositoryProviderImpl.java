package org.aincraft.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aincraft.config.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public final class ModelDataRepositoryProviderImpl implements
    Provider<Repository<String, ModelData.Record>> {

  private final YamlConfiguration configurationSection;

  @Inject
  public ModelDataRepositoryProviderImpl(YamlConfiguration configurationSection) {
    this.configurationSection = configurationSection;
  }

  @Override
  public Repository<String, ModelData.Record> get() {
    Map<String, ModelData.Record> modelData = new HashMap<>();

    for (String resourceKey : configurationSection.getKeys(false)) {
      ConfigurationSection blockSection = configurationSection.getConfigurationSection(resourceKey);
      if (blockSection == null) {
        continue;
      }

      ConfigurationSection modelSection = blockSection.getConfigurationSection("model");
      if (modelSection == null) {
        continue;
      }

      String itemModel = modelSection.getString("item-model", null);
      float range = (float) modelSection.getDouble("range", 0.0D);
      int block = modelSection.getInt("block", -1);
      int sky = modelSection.getInt("sky", -1);

      float[] translation = vector3(modelSection.getFloatList("translation"), 0.5f, 0.5f, 0.5f);
      float[] scale = vector3(modelSection.getFloatList("scale"), 1.0001f, 1.0001f, 1.0001f);
      float[] leftRot = quat4(modelSection.getFloatList("left-rotation"), 0f, 0f, 0f, 1f);
      float[] rightRot = quat4(modelSection.getFloatList("right-rotation"), 0f, 0f, 0f, 1f);

      ModelData.Record rec = new ModelData.Record(
          resourceKey,
          itemModel,
          translation[0], translation[1], translation[2],
          leftRot[0], leftRot[1], leftRot[2], leftRot[3],
          scale[0], scale[1], scale[2],
          rightRot[0], rightRot[1], rightRot[2], rightRot[3],
          range, block, sky
      );


      modelData.put(resourceKey, rec);
      Bukkit.getLogger().info(modelData.toString());
    }

    return new MemoryRepositoryImpl<>(modelData);
  }

  private static float[] vector3(List<Float> list,
      float dx, float dy, float dz) {
    float x = (list.size() > 0) ? list.get(0) : dx;
    float y = (list.size() > 1) ? list.get(1) : dy;
    float z = (list.size() > 2) ? list.get(2) : dz;
    return new float[]{x, y, z};
  }

  private static float[] quat4(List<Float> list,
      float dx, float dy, float dz, float dw) {
    float x = (list.size() > 0) ? list.get(0) : dx;
    float y = (list.size() > 1) ? list.get(1) : dy;
    float z = (list.size() > 2) ? list.get(2) : dz;
    float w = (list.size() > 3) ? list.get(3) : dw;
    return new float[]{x, y, z, w};
  }
}
