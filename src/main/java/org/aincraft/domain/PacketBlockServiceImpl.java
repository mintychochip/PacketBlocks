package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.Locale;
import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockBinding.Record;
import org.aincraft.api.BlockModel;
import org.aincraft.api.ModelData;
import org.aincraft.api.PacketBlock;
import org.aincraft.api.SoundData;
import org.aincraft.api.SoundData.SoundType;
import org.aincraft.api.SoundEntry;
import org.aincraft.api.SoundEntry.RecordKey;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

final class PacketBlockServiceImpl implements PacketBlockService {

  private final BlockModelService blockModelService;
  private final Repository<String, SoundData.Record> soundDataRepository;
  private final Mapper<SoundData, SoundData.Record> soundDataMapper;
  private final BlockBindingRepository blockBindingRepository;
  private final Mapper<BlockBinding, BlockBinding.Record> blockBindingMapper;

  @Inject
  public PacketBlockServiceImpl(
      BlockModelService blockModelService, Repository<String, SoundData.Record> soundDataRepository,
      Mapper<SoundData, SoundData.Record> soundDataMapper,
      BlockBindingRepository blockBindingRepository,
      Mapper<BlockBinding, BlockBinding.Record> blockBindingMapper) {
    this.blockModelService = blockModelService;
    this.soundDataRepository = soundDataRepository;
    this.soundDataMapper = soundDataMapper;
    this.blockBindingRepository = blockBindingRepository;
    this.blockBindingMapper = blockBindingMapper;
  }

  @Override
  public boolean delete(Location location) {
    if (!blockBindingRepository.delete(location)) {
      return false;
    }
    return blockModelService.delete(location);
  }

  @Override
  public PacketBlock save(BlockBinding blockBinding) {
    blockBindingRepository.save(blockBindingMapper.asRecord(blockBinding));
    blockModelService.save(blockBinding);
    return new PacketBlock() {
      @Override
      public BlockModel model() {
        return blockModelService.load(blockBinding.location());
      }

      @Override
      public ModelData modelData() {
        return blockBinding.blockData();
      }

      @Override
      public SoundData soundData() {
        return soundDataMapper.asDomain(
            soundDataRepository.load(blockBinding.resourceKey().toString()));
      }
    };
  }

  @Override
  public @Nullable PacketBlock load(Location location) {
    BlockModel model = blockModelService.load(location);
    Record record = blockBindingRepository.load(location);
    if (record == null) {
      return null;
    }
    BlockBinding blockBinding = blockBindingMapper.asDomain(record);
    if (model == null) {
      blockModelService.save(blockBinding);
      model = blockModelService.load(location);
    }
    BlockModel finalModel = model;
    return new PacketBlock() {
      @Override
      public BlockModel model() {
        return finalModel;
      }

      @Override
      public ModelData modelData() {
        return blockBinding.blockData();
      }

      @Override
      public SoundData soundData() {
        return soundDataMapper.asDomain(
            soundDataRepository.load(blockBinding.resourceKey().toString()));
      }
    };
  }
}
