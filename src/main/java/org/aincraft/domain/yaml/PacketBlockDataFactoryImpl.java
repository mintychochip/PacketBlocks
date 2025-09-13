package org.aincraft.domain.yaml;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import org.aincraft.api.ItemData;
import org.aincraft.api.ModelData;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.SoundData;
import org.aincraft.config.ConfigurationFactory;
import org.aincraft.domain.PacketBlockDataImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PacketBlockDataFactoryImpl implements ConfigurationFactory<PacketBlockData> {

  private final ConfigurationFactory<ModelData> modelDataFactory;
  private final ConfigurationFactory<ItemData> itemDataFactory;
  private final ConfigurationFactory<SoundData> soundDataFactory;

  @Inject
  PacketBlockDataFactoryImpl(ConfigurationFactory<ModelData> modelDataFactory,
      ConfigurationFactory<ItemData> itemDataFactory,
      ConfigurationFactory<SoundData> soundDataFactory) {
    this.modelDataFactory = modelDataFactory;
    this.itemDataFactory = itemDataFactory;
    this.soundDataFactory = soundDataFactory;
  }

  @Override
  public @NotNull PacketBlockData create(@Nullable ConfigurationSection section)
      throws IllegalArgumentException {
    Preconditions.checkArgument(section != null);
    Key resourceKey = NamespacedKey.fromString(section.getName());
    ModelData modelData = modelDataFactory.create(section.getConfigurationSection("model"));
    ItemData itemData = itemDataFactory.create(section.getConfigurationSection("item"));
    SoundData soundData = soundDataFactory.create(section.getConfigurationSection("sounds"));
    return new PacketBlockDataImpl(resourceKey, modelData, itemData, soundData);
  }
}
