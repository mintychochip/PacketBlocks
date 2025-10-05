package org.aincraft.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.aincraft.BlockBinding;
import org.aincraft.BlockBindingRepository;
import org.aincraft.BlockItemMeta;
import org.aincraft.BlockModelData;
import org.aincraft.ItemFactory;
import org.aincraft.registry.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

final class GetItemCommandImpl extends AbstractCommandImpl {

  private static final String AMOUNT_IDENTIFIER = "amount";
  private static final String RESOURCE_KEY_IDENTIFIER = "resource_key";
  private final ItemFactory itemFactory;
  private final Registry<BlockModelData> blockModelDataRegistry;
  private final Registry<BlockItemMeta> blockItemMetaRegistry;

  @Inject
  GetItemCommandImpl(BlockBindingRepository blockBindingRepository,
      ItemFactory itemFactory,
      Registry<BlockModelData> blockModelDataRegistry,
      Registry<BlockItemMeta> blockItemMetaRegistry) {
    super(blockBindingRepository);
    this.itemFactory = itemFactory;
    this.blockModelDataRegistry = blockModelDataRegistry;
    this.blockItemMetaRegistry = blockItemMetaRegistry;
  }

  @Override
  public LiteralArgumentBuilder<CommandSourceStack> build() {
    return Commands.literal("get")
        .executes(context -> execute(context, 1))
        .then(Commands.argument(AMOUNT_IDENTIFIER, IntegerArgumentType.integer(1))
            .executes(context -> {
              Integer amount = context.getArgument(AMOUNT_IDENTIFIER, Integer.class);
              return execute(context, amount);
            }))
        .then(Commands.argument(RESOURCE_KEY_IDENTIFIER, ArgumentTypes.key())
            .suggests((context, builder) -> {
              blockItemMetaRegistry.keySet().forEach(key -> builder.suggest(key.toString()));
              return builder.buildFuture();
            }).executes(context -> itemFromResourceKey(context, 1))
            .then(Commands.argument(AMOUNT_IDENTIFIER, IntegerArgumentType.integer(1))
                .executes(context -> {
                  int amount = context.getArgument(AMOUNT_IDENTIFIER, Integer.class);
                  return itemFromResourceKey(context, amount);
                })
            )
        );
  }

  private int itemFromResourceKey(CommandContext<CommandSourceStack> context, int amount) {
    return itemFromResourceKey(context, context.getArgument(RESOURCE_KEY_IDENTIFIER, Key.class),
        amount);
  }

  private int itemFromResourceKey(CommandContext<CommandSourceStack> context, Key resourceKey,
      int amount) {
    CommandSender sender = context.getSource().getSender();
    if (sender instanceof Player player) {
      ItemStack stack = itemFactory.create(resourceKey, amount);
      message(amount, stack.getData(DataComponentTypes.ITEM_NAME), player.getName());
      player.getInventory().addItem(stack);
      return PacketBlockCommand.SUCCESS;
    }
    return PacketBlockCommand.FAIL;
  }

  private int execute(CommandContext<CommandSourceStack> context, int amount)
      throws CommandSyntaxException {
    CommandSender sender = context.getSource().getSender();
    if (!(sender instanceof Player player)) {
      sender.sendMessage("Must be a player");
      return PacketBlockCommand.FAIL;
    }
    try {
      BlockBinding binding = rayTracePacketBlock(player);
      itemFromResourceKey(context, NamespacedKey.fromString(binding.resourceKey()), amount);
    } catch (BindingException ex) {
      Block block = ex.getBlock();
      ItemStack stack = ItemStack.of(block.getType(), amount);
      player.getInventory().addItem(stack);
      sender.sendRichMessage(
          message(amount, stack.getData(DataComponentTypes.ITEM_NAME), player.getName()));
    }
    return PacketBlockCommand.SUCCESS;
  }

  private static String message(int amount, Component displayName, String playerName) {
    String itemName = PlainTextComponentSerializer.plainText().serialize(displayName);
    return "[PacketBlocks] Gave %d [%s] to %s".formatted(amount, itemName, playerName);
  }

  @Override
  protected double rayTraceDistance() {
    return 15.0;
  }
}
