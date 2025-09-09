package org.aincraft.loot;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public record LootContextImpl(@Nullable ItemStack tool, HumanEntity entity) implements LootContext {

  @Override
  public int fortuneModifier() {
    return tool != null ? tool.getEnchantmentLevel(Enchantment.LOOTING) : 0;
  }

  @Override
  public boolean silkTouch() {
    return tool != null && tool.getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0;
  }

  @Override
  public double luck() {
    AttributeInstance attribute = entity.getAttribute(Attribute.LUCK);
    if (attribute == null) {
      return 0;
    }
    return attribute.getValue();
  }
}
