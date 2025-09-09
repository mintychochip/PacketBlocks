package org.aincraft.loot;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class VanillaLootContextAdapterImpl implements LootContext {

  private final org.bukkit.loot.LootContext delegate;

  public VanillaLootContextAdapterImpl(org.bukkit.loot.LootContext delegate) {
    this.delegate = delegate;
  }

  @Override
  public int fortuneModifier() {
    return delegate.getLootingModifier()
  }

  @Override
  public boolean silkTouch() {
    return false;
  }

  @Override
  public @Nullable ItemStack tool() {
    return null;
  }

  @Override
  public double luck() {
    return 0;
  }

  @Override
  public HumanEntity entity() {
    return null;
  }
}
