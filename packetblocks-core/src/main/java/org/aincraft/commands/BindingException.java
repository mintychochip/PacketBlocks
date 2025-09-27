package org.aincraft.commands;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

final class BindingException extends CommandSyntaxException {

  @NotNull
  private final Block block;

  public BindingException(CommandExceptionType type,
      Message message, @NotNull Block block) {
    super(type, message);
    this.block = block;
  }

  public static BindingException create(String message, Block block) {
    return new BindingException(new SimpleCommandExceptionType(new LiteralMessage(message)),
        new LiteralMessage(message), block);
  }

  public @NotNull Block getBlock() {
    return block;
  }
}
