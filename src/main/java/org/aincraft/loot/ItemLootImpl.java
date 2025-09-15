package org.aincraft.loot;

import io.papermc.paper.registry.RegistryAccess;
import java.util.HashMap;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

final class ItemLootImpl extends LootImpl implements Loot {

  private final Key itemKey;

  ItemLootImpl(int min, int max, Key itemKey) {
    super(min, max);
    this.itemKey = itemKey;
  }

  public Key getItemKey() {
    return itemKey;
  }

  private record ItemLootInstanceImpl(int amount, ItemStack itemStack) implements LootInstance {

    @Override
    public void add(Player player) {
      PlayerInventory inventory = player.getInventory();
      HashMap<Integer, ItemStack> missing = inventory.addItem(itemStack);
      Location location = player.getLocation();
      World world = location.getWorld();
      missing.forEach((__,item) -> {
        world.dropItemNaturally(location,item);
      });
    }
  }

  @Override
  public LootInstance instance(int amount) throws IllegalStateException {
    String namespace = itemKey.namespace();
    if ("minecraft".equals(namespace)) {
      Material material = Registry.MATERIAL.getOrThrow(itemKey);
      return new ItemLootInstanceImpl(amount,ItemStack.of(material,amount));
    }
    return null;
  }
}
