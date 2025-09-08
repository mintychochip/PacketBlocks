package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aincraft.BlockModelImpl;
import org.aincraft.Mapper;
import org.aincraft.PacketItemService;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockBinding.Record;
import org.aincraft.api.BlockModel;
import org.aincraft.api.ModelData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServiceImpl implements Service {

  private final ClientBlockDataRepository blockDataRepository;
  private final BlockBindingRepository blockBindingRepository;
  private final PacketItemService packetItemService;
  private final Mapper<ModelData, ModelData.Record> blockDataRecordMapper;
  private final Mapper<BlockBinding, BlockBinding.Record> blockBindingRecordMapper;
  private final Map<Location, BlockModel> activeBlocks = new HashMap<>();

  @Inject
  public ServiceImpl(ClientBlockDataRepository blockDataRepository,
      BlockBindingRepository blockBindingRepository,
      PacketItemService packetItemService,
      Mapper<ModelData, ModelData.Record> blockDataRecordMapper,
      Mapper<BlockBinding, BlockBinding.Record> blockBindingRecordMapper) {
    this.blockDataRepository = blockDataRepository;
    this.blockBindingRepository = blockBindingRepository;
    this.packetItemService = packetItemService;
    this.blockDataRecordMapper = blockDataRecordMapper;
    this.blockBindingRecordMapper = blockBindingRecordMapper;
  }

  @Override
  public boolean saveBlockBinding(@NotNull ModelData blockData) {
    ModelData.Record record = blockDataRecordMapper.asRecord(blockData);
    return blockDataRepository.save(record);
  }

  @Override
  public boolean saveBlockBinding(@NotNull BlockBinding blockBinding) {
    BlockBinding.Record record = blockBindingRecordMapper.asRecord(blockBinding);
    return blockBindingRepository.save(record);
  }

  @Override
  public boolean isPacketItem(ItemStack itemStack) {
    return packetItemService.isPacketItem(itemStack);
  }

  @Override
  public @Nullable BlockModel getBlock(Location location) {
    Record record = blockBindingRepository.load(location);
    if (record == null) {
      return null;
    }
    BlockBinding binding = blockBindingRecordMapper.asDomain(record);
    return activeBlocks.computeIfAbsent(binding.location(),
        __ -> BlockModelImpl.create(binding.blockData(), location.getWorld(),
            location.toVector()));
  }

  @Override
  public List<BlockBinding> getBindings(Chunk chunk) {
    return blockBindingRepository.loadAllByChunk(chunk).stream()
        .map(blockBindingRecordMapper::asDomain).toList();
  }

  @Override
  public ModelData readPacketData(ItemStack itemStack) throws IllegalArgumentException {
    return packetItemService.readPacketData(itemStack);
  }
}
