package org.aincraft.domain;

import com.google.inject.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.aincraft.api.ItemData;
import org.aincraft.api.ItemDataImpl;
import org.aincraft.api.ItemDataRecord;
import org.aincraft.api.ModelData;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.SoundEntry;
import org.aincraft.config.YamlConfiguration;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PacketBlockDataRepositoryProviderImpl implements Provider<PacketBlockDataRepository> {

  private static final Key STONE = Key.key("minecraft:stone");
  private final YamlConfiguration configuration;

  public PacketBlockDataRepositoryProviderImpl(YamlConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public PacketBlockDataRepository get() {
    Map<String, PacketBlockData.Record> packetBlockDataRecords = new HashMap<>();
    for (String resourceKey : configuration.getKeys(false)) {
      ConfigurationSection resourceSection = configuration.getConfigurationSection(
          resourceKey);
      if (resourceSection == null) {
        continue;
      }
      ConfigurationSection modelSection = resourceSection.getConfigurationSection("model");
      ModelData modelData = modelData(resourceSection.getConfigurationSection("model"));
      ItemDataRecord.Builder itemDataBuilder = new ItemDataRecord.Builder();
      itemDataBuilder.itemModel("minecraft:stone")
          .material("minecraft:stone");
      ConfigurationSection itemSection = resourceSection.getConfigurationSection("item");
      if (itemSection != null) {
        String itemModel = itemSection.getString("item-model", "minecraft:stone");
        String material = itemSection.getString("material", "minecraft:stone");
        ItemDataRecord record = new ItemDataRecord(itemModel, material);
      }
      ConfigurationSection soundSection = resourceSection.getConfigurationSection("sound");
      if (soundSection != null) {
        Map<String, SoundEntry.Record> entries = new HashMap<>();
        for (String soundType : soundSection.getKeys(false)) {
          ConfigurationSection soundTypeSection = soundSection.getConfigurationSection(
              soundType);
          if (soundTypeSection == null) {
            continue;
          }
          String soundKey = soundTypeSection.getString("key", null);
          float volume = (float) soundTypeSection.getDouble("volume", 0.0);
          float pitch = (float) soundTypeSection.getDouble("pitch", 0.0);
          entries.put(soundType, new SoundEntry.Record(soundType, soundKey, volume, pitch));
        }
      }
      packetBlockDataRecords.put(resourceKey,
          new PacketBlockData.Record(resourceKey, builder.build(), ));
    }
    return new MemoryPacketBlockDataRepositoryImpl(packetBlockDataRecords);
  }

  private ModelData modelData(@Nullable ConfigurationSection section) {
    if (section == null) {
      return ModelData.DEFAULT;
    }

    Key itemModel = key(section.getString("item-model"),STONE);
    Vector3f translation = vector3f(section.getFloatList("translation"), new Vector3f(0.5f));
    Quaternionf rotation = quaternionf(section.getFloatList("rotation"), new Quaternionf());
    Vector3f scale = vector3f(section.getFloatList("scale"), new Vector3f(0.5f));
    float range = (float) section.getDouble("range", 32.0);
    int block = section.getInt("block", -1);
    int sky = section.getInt("sky", -1);

    return ModelData.DEFAULT.toBuilder()
        .itemModel(itemModel)
        .translation(translation)
        .rotation(rotation)
        .scale(scale)
        .range(range)
        .block(block)
        .sky(sky)
        .build();
  }

  private ItemData itemData(@Nullable ConfigurationSection section) {
    if (section == null) {
      return new ItemDataImpl()
    }
  }

  private static Key key(String key, Key def) {
    if (key == null) {
      return def;
    }
    return NamespacedKey.fromString(key);
  }

  private static Vector3f vector3f(List<Float> list, Vector3f def) {
    float x = (list.size() > 0) ? list.get(0) : def.x();
    float y = (list.size() > 1) ? list.get(1) : def.y();
    float z = (list.size() > 2) ? list.get(2) : def.z();
    return new Vector3f(x, y, z);
  }

  private static Quaternionf quaternionf(List<Float> list,
      Quaternionf def) {
    float x = (list.size() > 0) ? list.get(0) : def.x();
    float y = (list.size() > 1) ? list.get(1) : def.y();
    float z = (list.size() > 2) ? list.get(2) : def.z();
    float w = (list.size() > 3) ? list.get(3) : def.w();
    return new Quaternionf(x, y, z, w);
  }

  private record MemoryPacketBlockDataRepositoryImpl(
      Map<String, PacketBlockData.Record> data) implements
      PacketBlockDataRepository {

    @Override
    public @Nullable PacketBlockData.Record load(String resourceKey) {
      return data.get(resourceKey);
    }
  }
}
