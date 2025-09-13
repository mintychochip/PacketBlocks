package org.aincraft.domain;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aincraft.BlockModelImpl;
import org.aincraft.Mapper;
import org.aincraft.PacketItemService;
import org.aincraft.api.BlockModel;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServiceImpl implements Service {

  private final BlockBindingRepository blockBindingRepository;
  private final PacketItemService packetItemService;
  private final Map<Location, BlockModel> activeBlocks = new HashMap<>();

  @Inject
  public ServiceImpl(
      BlockBindingRepository blockBindingRepository,
      PacketItemService packetItemService) {
    this.blockBindingRepository = blockBindingRepository;
    this.packetItemService = packetItemService;
  }

  public boolean isPacketItem(ItemStack itemStack) {
    return packetItemService.isPacketItem(itemStack);
  }

  @Override
  public String readPacketData(ItemStack itemStack) throws IllegalArgumentException {
    return packetItemService.readPacketData(itemStack);
  }
}
