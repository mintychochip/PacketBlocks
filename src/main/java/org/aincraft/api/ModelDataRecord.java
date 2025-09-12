package org.aincraft.api;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public record ModelDataRecord(String itemModel, Vector3f translation,
                              Quaternionf rotation,
                              float scale,
                              float range,
                              int block, int sky) {

  public Builder builder() {
    return new Builder(itemModel, translation, rotation, scale, range, block, sky);
  }

  public static final class Builder {

    private String itemModel;
    private Vector3f translation;
    private Quaternionf rotation;
    private float scale, range;
    private int block, sky;

    public Builder(String itemModel, Vector3f translation, Quaternionf rotation,
        float scale, float range, int block, int sky) {
      this.itemModel = itemModel;
      this.translation = translation;
      this.rotation = rotation;
      this.scale = scale;
      this.range = range;
      this.block = block;
      this.sky = sky;
    }

    public Builder() {
    }

    public Builder itemModel(String itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    public Builder translation(Vector3f translation) {
      this.translation = translation;
      return this;
    }

    public Builder rotation(Quaternionf rotation) {
      this.rotation = rotation;
      return this;
    }

    public Builder scale(float scale) {
      this.scale = scale;
      return this;
    }

    public Builder range(float range) {
      this.range = range;
      return this;
    }

    public Builder block(int block) {
      this.block = block;
      return this;
    }

    public Builder sky(int sky) {
      this.sky = sky;
      return this;
    }

    public ModelDataRecord build() {
      return new ModelDataRecord(itemModel, translation, rotation, scale, range, block, sky);
    }
  }
}
