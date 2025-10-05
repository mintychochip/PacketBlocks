package org.aincraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
interface PacketBlockCommand {

  int FAIL = 0;
  int SUCCESS = Command.SINGLE_SUCCESS;

  LiteralArgumentBuilder<CommandSourceStack> build();
}
