package org.aincraft.loot;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface LootContext {

  int fortuneModifier();

  boolean silkTouch();
  @Nullable
  ItemStack tool();
  double luck();
  HumanEntity entity();
}
