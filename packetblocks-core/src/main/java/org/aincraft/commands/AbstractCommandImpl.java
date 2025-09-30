package org.aincraft.commands;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.aincraft.BlockBinding;
import org.aincraft.BlockBindingRepository;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

abstract class AbstractCommandImpl implements PacketBlockCommand {

  protected final BlockBindingRepository blockBindingRepository;

  protected abstract double rayTraceDistance();

  AbstractCommandImpl(BlockBindingRepository blockBindingRepository) {
    this.blockBindingRepository = blockBindingRepository;
  }

  @NotNull
  BlockBinding rayTracePacketBlock(Player player) throws CommandSyntaxException {
    RayTraceResult rayTraceResult = player.rayTraceBlocks(rayTraceDistance(),
        FluidCollisionMode.NEVER);
    if (rayTraceResult == null || rayTraceResult.getHitBlock() == null) {
      throw new SimpleCommandExceptionType(
          new LiteralMessage("Failed to locate a block within " + rayTraceDistance() + " blocks")
      ).create();
    }
    Block block = rayTraceResult.getHitBlock();
    BlockBinding binding = blockBindingRepository.load(block.getLocation());
    if (binding == null) {
      throw BindingException.create("The block at " + block.getLocation() + " is not bound to any resource", block);
    }
    return binding;
  }
}
