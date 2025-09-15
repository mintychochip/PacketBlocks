package org.aincraft.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import org.aincraft.loot.Loot.LootInstance;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public record LootDataImpl(boolean dropOnShear, boolean dropOnSilkTouch,
                           Map<Loot, Double> loot) implements LootData {

  @Override
  public List<LootInstance> get(LootContext context) {
    List<LootInstance> lootInstances = new ArrayList<>();
    for (Entry<Loot, Double> entry : loot.entrySet()) {
      Loot loot = entry.getKey();
      Double chance = entry.getValue();
      if (ThreadLocalRandom.current().nextDouble() <= chance) {
        int min = loot.min();
        int max = loot.max();
        int amount = ThreadLocalRandom.current().nextInt(min, max);
        lootInstances.add(loot.instance(amount));
      }
    }
    return lootInstances;
  }

  @Override
  public LootDataBuilder toBuilder() {
    return new BuilderImpl()
        .dropOnShear(dropOnShear)
        .dropOnSilkTouch(dropOnSilkTouch);
  }

  static final class BuilderImpl implements LootDataBuilder {

    private boolean dropOnSilkTouch, dropOnShear;
    private Map<Loot, Double> loot = new HashMap<>();

    @Override
    public LootDataBuilder addLoot(Loot loot, double weight) {
      this.loot.put(loot,weight);
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
      return new LootDataImpl(dropOnShear, dropOnSilkTouch, loot);
    }
  }
}
