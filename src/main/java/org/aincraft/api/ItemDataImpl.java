package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;

public class ItemDataImpl implements ItemData {

  static final ItemDataImpl DEFAULT = new Builder()
      .itemModel(Material.STONE.key())
      .material(Material.STONE)
      .build();

  private final Key itemModel;
  private final Material material;

  private ItemDataImpl(Key itemModel, Material material) {
    this.itemModel = itemModel;
    this.material = material;
  }

  @Override
  public Key itemModel() {
    return itemModel;
  }

  @Override
  public Material material() {
    return material;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private Key itemModel;
    private Material material;

    public Builder itemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    public Builder material(Material material) {
      this.material = material;
      return this;
    }

    public ItemDataImpl build() {
      return new ItemDataImpl(itemModel, material);
    }
  }
}
