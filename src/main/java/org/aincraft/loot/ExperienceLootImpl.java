package org.aincraft.loot;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

final class ExperienceLootImpl extends LootImpl implements Loot {

  ExperienceLootImpl(int min, int max) {
    super(min, max);
  }

  @Override
  public LootInstance instance(int amount) throws IllegalStateException {
    Preconditions.checkState(amount > 0);
    return new ExperienceLootInstanceImpl(amount);
  }

  private record ExperienceLootInstanceImpl(int amount) implements LootInstance {

    @Override
    public void add(Player player) {
      player.giveExp(amount);
    }
  }
}
