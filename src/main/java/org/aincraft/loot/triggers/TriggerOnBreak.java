package org.aincraft.loot.triggers;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public interface TriggerOnBreak {

  void onBreak(BlockBreakContext context);

  interface BlockBreakContext {

    void exp(int xp);

    int exp();
  }
}
