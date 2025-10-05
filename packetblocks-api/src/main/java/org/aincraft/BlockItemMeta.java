package org.aincraft;

import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockItemMeta extends Keyed {
  @Nullable
  Component displayName();
  Key itemModel();
  Material material();
  @Nullable
  List<? extends Component> itemLore();

  interface Builder {
    Builder displayName(Component displayName);
    Builder itemModel(Key itemModel);
    Builder material(Material material);
    Builder itemLore(List<? extends Component> itemLore);
    BlockItemMeta build();
  }
}