package org.aincraft;

import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public record BlockItemMetaImpl(Key resourceKey, Component displayName, Key itemModel,
                                Material material, List<? extends Component> itemLore) implements
    BlockItemMeta {

  @Override
  public @NotNull Key key() {
    return resourceKey;
  }

  static final class BuilderImpl implements BlockItemMeta.Builder {

    private final Key resourceKey;
    private Component displayName;
    private Key itemModel;
    private Material material;
    private List<? extends Component> itemLore = null;

    BuilderImpl(Key resourceKey) {
      this.resourceKey = resourceKey;
    }

    @Override
    public Builder displayName(Component displayName) {
      this.displayName = displayName;
      return this;
    }

    @Override
    public Builder itemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    @Override
    public Builder material(Material material) {
      this.material = material;
      return this;
    }

    @Override
    public Builder itemLore(List<? extends Component> itemLore) {
      return null;
    }

    @Override
    public BlockItemMeta build() {
      return new BlockItemMetaImpl(resourceKey, displayName, itemModel, material, itemLore);
    }
  }
}
