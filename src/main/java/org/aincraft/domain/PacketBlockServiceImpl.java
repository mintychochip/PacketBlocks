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
  private final BlockBindingRepository blockBindingRepository;
  private final Mapper<BlockBinding, BlockBinding.Record> blockBindingMapper;

  @Inject
  public PacketBlockServiceImpl(
      BlockModelService blockModelService,
      BlockBindingRepository blockBindingRepository,
      Mapper<BlockBinding, BlockBinding.Record> blockBindingMapper) {
    this.blockModelService = blockModelService;
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
    boolean saved = blockModelService.save(blockBinding);
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
        return null;
      }
    };
  }

  @Override
  public @Nullable PacketBlock load(Location location) {
    BlockModel model = blockModelService.load(location);
    if (model == null) {
      return null;
    }
    Record record = blockBindingRepository.load(location);
    if (record == null) {
      return null;
    }
    BlockBinding blockBinding = blockBindingMapper.asDomain(record);
    return new PacketBlock() {
      @Override
      public BlockModel model() {
        return model;
      }

      @Override
      public ModelData modelData() {
        return blockBinding.blockData();
      }

      @Override
      public SoundData soundData() {
        return null;
      }
    };
  }
}
