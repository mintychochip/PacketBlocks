package org.aincraft.loot;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.aincraft.loot.Loot.LootInstance;
import org.bukkit.inventory.ItemStack;

public record LootDataImpl(boolean dropOnShear, boolean dropOnSilkTouch,
                           WeightedRandom<Loot> lootRandom) implements LootData {
  @Override
  public List<ItemStack> get(LootContext context) {
    Loot loot = lootRandom.get();
    int min = loot.min();
    int max = loot.max();
    int amount = ThreadLocalRandom.current().nextInt(min, max);
    LootInstance instance = loot.instance(amount);
    return List.of();
  }

  @Override
  public LootDataBuilder toBuilder() {
    return new BuilderImpl()
        .dropOnShear(dropOnShear)
        .dropOnSilkTouch(dropOnSilkTouch);
  }

  static final class BuilderImpl implements LootDataBuilder {

    private boolean dropOnSilkTouch, dropOnShear;
    private WeightedRandom<Loot> random = WeightedRandom.create();

    @Override
    public LootDataBuilder addLoot(Loot loot, double weight) {
      random.add(loot, weight);
      return this;
    }

    @Override
    public LootDataBuilder dropOnSilkTouch(boolean dropOnSilkTouch) {
      this.dropOnSilkTouch = dropOnSilkTouch;
      return this;
    }

    @Override
    public LootDataBuilder dropOnShear(boolean dropOnShear) {
      this.dropOnShear = dropOnShear;
      return this;
    }

    @Override
    public LootData build() {
      return new LootDataImpl(dropOnShear, dropOnSilkTouch, random);
    }
  }
}
