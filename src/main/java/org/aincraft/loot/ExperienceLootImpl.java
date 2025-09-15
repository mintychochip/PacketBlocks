package org.aincraft.loot;

import com.google.common.base.Preconditions;
import org.aincraft.loot.triggers.TriggerOnBreak;

final class ExperienceLootImpl extends LootImpl implements Loot {

  ExperienceLootImpl(int min, int max) {
    super(min, max);
  }

  @Override
  public LootInstance instance(int amount) throws IllegalStateException {
    Preconditions.checkState(amount > 0);
    return new ExperienceLootInstanceImpl(amount);
  }

  private record ExperienceLootInstanceImpl(int amount) implements LootInstance, TriggerOnBreak {

    @Override
    public void onBreak(BlockBreakContext context) {
      context.exp(amount + context.exp());
    }
  }
}
