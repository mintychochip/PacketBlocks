package org.aincraft.api;

import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModelDataImpl implements ModelData {

  static final ModelDataImpl DEFAULT = new ModelDataBuilder()
      .itemModel(Material.STONE.key())
      .translation(new Vector3f(0.5f))
      .scale(new Vector3f(1.0f))
      .rotation(new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f))
      .range(32.0f)
      .block(-1)
      .sky(-1)
      .build();

  private final Key itemModel;
  private final Vector3f scale;
  private final Vector3f translation;
  private final Quaternionf rotation;
  private final float range;
  private final float block;
  private final float sky;

  private ModelDataImpl(Key itemModel,
      Vector3f scale,
      Vector3f translation,
      Quaternionf rotation,
      float range,
      float block,
      float sky) {
    this.itemModel = itemModel;
    this.scale = scale;
    this.translation = translation;
    this.rotation = rotation;
    this.range = range;
    this.block = block;
    this.sky = sky;
  }

  @Override
  public Key itemModel() {
    return itemModel;
  }

  @Override
  public Vector3f scale() {
    return scale;
  }

  @Override
  public Vector3f translation() {
    return translation;
  }

  @Override
  public Quaternionf rotation() {
    return rotation;
  }

  @Override
  public float range() {
    return range;
  }

  @Override
  public float block() {
    return block;
  }

  @Override
  public float sky() {
    return sky;
  }

  public ModelDataBuilder toBuilder() {
    return new ModelDataBuilder()
        .itemModel(itemModel)
        .translation(new Vector3f(translation))
        .scale(new Vector3f(scale))
        .rotation(new Quaternionf(rotation))
        .range(range)
        .block(block)
        .sky(sky);
  }

  public static final class ModelDataBuilder implements ModelData.ModelDataBuilder {

    private Key itemModel;
    private Vector3f scale = new Vector3f(1.0f);
    private Vector3f translation = new Vector3f();
    private Quaternionf rotation = new Quaternionf();
    private float range = 0f;
    private float block = 0f;
    private float sky = 0f;

    public ModelDataBuilder itemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    public ModelDataBuilder scale(Vector3f scale) {
      this.scale = scale;
      return this;
    }

    public ModelDataBuilder translation(Vector3f translation) {
      this.translation = translation;
      return this;
    }

    public ModelDataBuilder rotation(Quaternionf rotation) {
      this.rotation = rotation;
      return this;
    }

    public ModelDataBuilder range(float range) {
      this.range = range;
      return this;
    }

    public ModelDataBuilder block(float block) {
      this.block = block;
      return this;
    }

    public ModelDataBuilder sky(float sky) {
      this.sky = sky;
      return this;
    }

    public ModelDataImpl build() {
      return new ModelDataImpl(itemModel, scale, translation, rotation, range, block, sky);
    }
  }
}
