package org.aincraft.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.Set;

public final class RootCommand implements PacketBlockCommand {

  private final Set<PacketBlockCommand> commands;

  @Inject
  RootCommand(Set<PacketBlockCommand> commands) {
    this.commands = commands;
  }

  @Override
  public LiteralArgumentBuilder<CommandSourceStack> build() {
    LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("pb");
    commands.forEach(command -> {
      root.then(command.build());
    });
    return root;
  }
}
