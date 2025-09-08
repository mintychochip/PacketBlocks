package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aincraft.ClientBlockImpl;
import org.aincraft.Mapper;
import org.aincraft.PacketItemService;
import org.aincraft.api.BlockBinding;
import org.aincraft.api.BlockBinding.Record;
import org.aincraft.api.ClientBlock;
import org.aincraft.api.ClientBlockData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServiceImpl implements Service {

  private final ClientBlockDataRepository blockDataRepository;
  private final BlockBindingRepository blockBindingRepository;
  private final PacketItemService packetItemService;
  private final Mapper<ClientBlockData, ClientBlockData.Record> blockDataRecordMapper;
  private final Mapper<BlockBinding, BlockBinding.Record> blockBindingRecordMapper;
  private final Map<Location, ClientBlock> activeBlocks = new HashMap<>();

  @Inject
  public ServiceImpl(ClientBlockDataRepository blockDataRepository,
      BlockBindingRepository blockBindingRepository,
      PacketItemService packetItemService,
      Mapper<ClientBlockData, ClientBlockData.Record> blockDataRecordMapper,
      Mapper<BlockBinding, BlockBinding.Record> blockBindingRecordMapper) {
    this.blockDataRepository = blockDataRepository;
    this.blockBindingRepository = blockBindingRepository;
    this.packetItemService = packetItemService;
    this.blockDataRecordMapper = blockDataRecordMapper;
    this.blockBindingRecordMapper = blockBindingRecordMapper;
  }

  @Override
  public boolean saveBlockBinding(@NotNull ClientBlockData blockData) {
    ClientBlockData.Record record = blockDataRecordMapper.asRecord(blockData);
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
  public @Nullable ClientBlock getBlock(Location location) {
    Record record = blockBindingRepository.load(location);
    if (record == null) {
      return null;
    }
    BlockBinding binding = blockBindingRecordMapper.asDomain(record);
    return activeBlocks.computeIfAbsent(binding.location(),
        __ -> ClientBlockImpl.create(binding.blockData(), location.getWorld(),
            location.toVector()));
  }

  @Override
  public List<BlockBinding> getBindings(Chunk chunk) {
    return blockBindingRepository.loadAllByChunk(chunk).stream()
        .map(blockBindingRecordMapper::asDomain).toList();
  }

  @Override
  public ClientBlockData readPacketData(ItemStack itemStack) throws IllegalArgumentException {
    return packetItemService.readPacketData(itemStack);
  }
}
