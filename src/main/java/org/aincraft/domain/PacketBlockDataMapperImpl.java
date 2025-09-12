package org.aincraft.domain;

import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.ItemData;
import org.aincraft.api.ItemDataRecord;
import org.aincraft.api.ModelData;
import org.aincraft.api.ModelDataRecord;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.PacketBlockData.Record;
import org.aincraft.api.SoundData;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

final class PacketBlockDataMapperImpl implements Mapper<PacketBlockData, Record> {

  private final Mapper<ItemData, ItemDataRecord> itemDataMapper;
  private final Mapper<ModelData, ModelDataRecord> modelDataMapper;
  private final Mapper<SoundData, SoundData.Record> soundDataMapper;

  @Inject
  PacketBlockDataMapperImpl(Mapper<ItemData, ItemDataRecord> itemDataMapper,
      Mapper<ModelData, ModelDataRecord> modelDataMapper,
      Mapper<SoundData, SoundData.Record> soundDataMapper) {
    this.itemDataMapper = itemDataMapper;
    this.modelDataMapper = modelDataMapper;
    this.soundDataMapper = soundDataMapper;
  }

  @Override
  public @NotNull PacketBlockData asDomain(@NotNull Record record) throws IllegalArgumentException {
    Key resourceKey = NamespacedKey.fromString(record.resourceKey());
    ModelData modelData = modelDataMapper.asDomain(record.modelDataRecord());
    ItemData itemData = itemDataMapper.asDomain(record.itemDataRecord());
    SoundData soundData = soundDataMapper.asDomain(record.soundDataRecord());
    return new PacketBlockData() {
      @Override
      public Key resourceKey() {
        return resourceKey;
      }

      @Override
      public ModelData modelData() {
        return modelData;
      }

      @Override
      public ItemData itemData() {
        return itemData;
      }

      @Override
      public SoundData soundData() {
        return soundData;
      }
    };
  }

  @Override
  public @NotNull Record asRecord(@NotNull PacketBlockData domain) throws IllegalArgumentException {
    String resourceKey = domain.resourceKey().toString();
    ModelDataRecord modelData = modelDataMapper.asRecord(domain.modelData());
    SoundData.Record soundData = soundDataMapper.asRecord(domain.soundData());
    ItemDataRecord itemData = itemDataMapper.asRecord(domain.itemData());
    return new Record(resourceKey, modelData, soundData, itemData);
  }
}
