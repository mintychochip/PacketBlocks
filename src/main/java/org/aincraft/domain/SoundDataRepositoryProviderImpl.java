package org.aincraft.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import org.aincraft.config.YamlConfiguration;

public final class SoundDataRepositoryProviderImpl implements
    Provider<Repository<String, SoundData.Record>> {

  private final YamlConfiguration configuration;

  @Inject
  public SoundDataRepositoryProviderImpl(YamlConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public Repository<String, Record> get() {
    Map<String, Record> soundData = new HashMap<>();

    for (String resourceKey : configuration.getKeys(false)) {
      var itemSection = configuration.getConfigurationSection(resourceKey);
      if (itemSection == null) continue;

      var soundsSection = itemSection.getConfigurationSection("sounds");
      if (soundsSection == null) continue;

      Map<String, org.aincraft.api.SoundEntry.Record> entries = new HashMap<>();

      for (String soundType : soundsSection.getKeys(false)) {
        var typeSection = soundsSection.getConfigurationSection(soundType);
        if (typeSection == null) continue;

        String name = typeSection.getString("name");
        if (name == null || name.isBlank()) continue; // skip if no sound key

        float volume = (float) typeSection.getDouble("volume", 1.0d);
        float pitch  = (float) typeSection.getDouble("pitch", 1.0d);

        entries.put(
            soundType, // e.g., "break", "place"
            new org.aincraft.api.SoundEntry.Record(
                resourceKey,
                soundType,
                name,
                volume,
                pitch
            )
        );
      }

      if (!entries.isEmpty()) {
        soundData.put(resourceKey, new Record(resourceKey, entries));
      }
    }

    return new MemoryRepositoryImpl<>(soundData);
  }
}
