package org.aincraft.domain;

import org.aincraft.Mapper;
import org.aincraft.api.PacketBlockData;
import org.aincraft.api.PacketBlockData.Record;
import org.jetbrains.annotations.Nullable;

public class PacketBlockDataServiceImpl implements PacketBlockDataService {

  private final Repository<String, PacketBlockData.Record> packetBlockDataRepository;
  private final Mapper<PacketBlockData, Record> packetBlockDataMapper;

  public PacketBlockDataServiceImpl(
      Repository<String, PacketBlockData.Record> packetBlockDataRepository,
      Mapper<PacketBlockData, Record> packetBlockDataMapper) {
    this.packetBlockDataRepository = packetBlockDataRepository;
    this.packetBlockDataMapper = packetBlockDataMapper;
  }

  @Override
  public @Nullable PacketBlockData load(String resourceKey) {
    Record load = packetBlockDataRepository.load(resourceKey);
    if (load == null) {
      return null;
    }
    return packetBlockDataMapper.asDomain(load);
  }
}
