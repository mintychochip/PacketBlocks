package org.aincraft.api;

import java.util.function.Consumer;

public record ItemDataRecord(String itemModel, String material) {

  public static final class Builder {
    private String itemModel;
    private String material;

    public Builder(String itemModel, String material) {
      this.itemModel = itemModel;
      this.material = material;
    }

    public Builder() {
    }

    public Builder itemModel(String itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    public Builder material(String material) {
      this.material = material;
      return this;
    }

    public ItemDataRecord build() {
      return new ItemDataRecord(itemModel, material);
    }
  }
}
