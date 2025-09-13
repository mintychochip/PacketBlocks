package org.aincraft.domain.yaml;

import java.util.Collections;
import java.util.Locale;
import org.aincraft.api.SoundData;
import org.aincraft.api.SoundData.SoundDataBuilder;
import org.aincraft.api.SoundData.SoundType;
import org.aincraft.api.SoundEntryImpl;
import org.aincraft.config.ConfigurationFactory;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SoundDataFactoryImpl implements ConfigurationFactory<SoundData> {

  private static final SoundData DEFAULT = SoundData.builder().entries(Collections.emptyMap())
      .build();

  @Override
  public @NotNull SoundData create(@Nullable ConfigurationSection section)
      throws IllegalArgumentException {
    if (section == null) {
      return DEFAULT;
    }
    SoundDataBuilder builder = DEFAULT.toBuilder();
    for (String soundType : section.getKeys(false)) {
      SoundType type = SoundType.valueOf(soundType.toUpperCase(Locale.ENGLISH));
      ConfigurationSection soundTypeSection = section.getConfigurationSection(soundType);
      if (soundTypeSection == null) {
        continue;
      }
      String soundKey = soundTypeSection.getString("key");
      if (soundKey == null) {
        continue;
      }
      double volume = soundTypeSection.getDouble("volume",1.0);
      double pitch = soundTypeSection.getDouble("pitch", 1.0);
      builder.entry(type, new SoundEntryImpl(type, NamespacedKey.fromString(soundKey),
          (float) volume,
          (float) pitch));
    }
    return builder.build();
  }
}
