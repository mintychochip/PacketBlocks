package org.aincraft.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.datacomponent.DataComponentTypes;
import java.util.NoSuchElementException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.aincraft.BlockBinding;
import org.aincraft.BlockBindingRepository;
import org.aincraft.BlockModelData;
import org.aincraft.ItemService;
import org.aincraft.registry.Registry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

final class GetItemCommandImpl extends AbstractCommandImpl {

  private final ItemService itemService;
  private final Registry<BlockModelData> blockModelDataRegistry;

  @Inject
  GetItemCommandImpl(BlockBindingRepository blockBindingRepository,
      ItemService itemService, Registry<BlockModelData> blockModelDataRegistry) {
    super(blockBindingRepository);
    this.itemService = itemService;
    this.blockModelDataRegistry = blockModelDataRegistry;
  }

  @Override
  public LiteralArgumentBuilder<CommandSourceStack> build() {
    return Commands.literal("get")
        .executes(context -> execute(context,1))
        .then(Commands.argument("amount", IntegerArgumentType.integer(1)))
        .executes(context -> {
          Integer amount = context.getArgument("amount", Integer.class);
          return execute(context,amount);
        });
  }

  private int execute(CommandContext<CommandSourceStack> context, int count)
      throws CommandSyntaxException {
    CommandSender sender = context.getSource().getSender();
    if (!(sender instanceof Player player)) {
      sender.sendMessage("Must be a player");
      return PacketBlockCommand.FAIL;
    }
    try {
      BlockBinding binding = rayTracePacketBlock(player);
      BlockModelData modelData = blockModelDataRegistry.get(
          NamespacedKey.fromString(binding.resourceKey()));
      ItemStack stack = ItemStack.of(Material.STONE);
      stack.setData(DataComponentTypes.ITEM_MODEL, modelData.itemModel());
      itemService.write(stack, binding.resourceKey());
      player.getInventory().addItem(stack);
      sender.sendRichMessage(message(count, PlainTextComponentSerializer.plainText()
          .serialize(stack.getData(DataComponentTypes.ITEM_NAME)), player.getName()));
    } catch (BindingException ex) {
      Block block = ex.getBlock();
      player.getInventory().addItem(ItemStack.of(block.getType()));
    }
    return PacketBlockCommand.SUCCESS;
  }

  private static String message(int amount, String itemName, String playerName) {
    return "[PacketBlocks] Gave %d [%s] to %s".formatted(amount, itemName, playerName);
  }

  @Override
  protected double rayTraceDistance() {
    return 15.0;
  }
}
