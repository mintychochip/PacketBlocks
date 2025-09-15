package org.aincraft.loot;

import java.util.List;
import org.aincraft.api.Builder;
import org.aincraft.api.Builder.Buildable;
import org.aincraft.loot.LootData.LootDataBuilder;
import org.aincraft.loot.LootDataImpl.BuilderImpl;
import org.bukkit.inventory.ItemStack;

public interface LootData extends Buildable<LootDataBuilder, LootData> {

  static LootDataBuilder builder() {
    return new BuilderImpl();
  }

  boolean dropOnSilkTouch();

  boolean dropOnShear();

  List<ItemStack> get(LootContext context);

  interface LootDataBuilder extends Builder<LootData> {

    LootDataBuilder addLoot(Loot loot, double weight);

    LootDataBuilder dropOnSilkTouch(boolean dropOnSilkTouch);

    LootDataBuilder dropOnShear(boolean dropOnShear);
  }
}
