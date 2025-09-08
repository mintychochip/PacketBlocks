package org.aincraft.domain;

import net.kyori.adventure.key.Key;
import net.minecraft.util.Brightness;
import org.aincraft.api.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record ModelDataImpl(
    Key resourceKey,
    Key itemModel,
    Vector3f translation, Quaternionf leftRotation, Vector3f scale,
    Quaternionf rightRotation,
    float range,
    @Nullable Brightness brightness
) implements ModelData {

  private static final Key DEFAULT_ITEM_MODEL;
  private static final Vector3f DEFAULT_TRANSLATION, DEFAULT_SCALE;
  private static final Quaternionf DEFAULT_ROTATION;
  private static final float DEFAULT_RANGE;

  static {
    float scale = 1.000001f;
    float translation = (1.0f - scale) / 2.0f;
    DEFAULT_ITEM_MODEL = Key.key("minecraft:stone");
    DEFAULT_SCALE = new Vector3f(scale, scale, scale);
    DEFAULT_TRANSLATION = new Vector3f(translation, translation, translation);
    DEFAULT_ROTATION = new Quaternionf(0, 0, 0, 1);
    DEFAULT_RANGE = 32.0f;
  }

  public ModelDataImpl(Key resourceKey) {
    this(resourceKey, DEFAULT_ITEM_MODEL, DEFAULT_TRANSLATION, DEFAULT_ROTATION,
        DEFAULT_SCALE,
        DEFAULT_ROTATION, DEFAULT_RANGE, null);
  }

  public Builder builder() {
    return new BuilderImpl(this);
  }

  @Override
  public ModelData brightness(int block, int sky) {
    return new BuilderImpl(this)
        .setBrightness(block, sky)
        .build();
  }

  @Override
  public ModelData scale(Vector3f scale) {
    return new BuilderImpl(this)
        .setScale(scale)
        .build();
  }

  @Override
  public ModelData translation(Vector3f translation) {
    return new BuilderImpl(this)
        .setTranslation(translation)
        .build();
  }

  @Override
  public ModelData leftRotation(Quaternionf leftRotation) {
    return new BuilderImpl(this)
        .setLeftRotation(leftRotation)
        .build();
  }

  @Override
  public ModelData rightRotation(Quaternionf rightRotation) {
    return new BuilderImpl(this)
        .setRightRotation(rightRotation)
        .build();
  }

  @Override
  public ModelData itemModel(Key key) {
    return new BuilderImpl(this)
        .setItemModel(key)
        .build();
  }

  @Override
  public int blockLight() {
    return brightness != null ? brightness.block() : -1;
  }

  @Override
  public int skyLight() {
    return brightness != null ? brightness.sky() : -1;
  }

  @Override
  public ModelData range(float range) {
    return new BuilderImpl(this)
        .setRange(range)
        .build();
  }


  private static final class BuilderImpl implements Builder {

    private Key resourceKey;
    private Key itemModel;
    private Vector3f scale, translation;
    private Quaternionf leftRotation, rightRotation;
    @Nullable
    private Brightness brightness;
    private float range;

    BuilderImpl(ModelData blockData) {
      this.resourceKey = blockData.resourceKey();
      this.itemModel = blockData.itemModel();
      this.scale = blockData.scale();
      this.translation = blockData.translation();
      this.leftRotation = blockData.leftRotation();
      this.rightRotation = blockData.rightRotation();
      this.range = blockData.range();
      this.brightness = asNMSBrightness(blockData.blockLight(), blockData.skyLight());
    }

    @Override
    public Builder setItemModel(Key itemModel) {
      this.itemModel = itemModel;
      return this;
    }

    @Override
    public Builder setScale(Vector3f scale) {
      this.scale = scale;
      return this;
    }

    @Override
    public Builder setTranslation(Vector3f translation) {
      this.translation = translation;
      return this;
    }

    @Override
    public Builder setLeftRotation(Quaternionf leftRotation) {
      this.leftRotation = leftRotation;
      return this;
    }

    @Override
    public Builder setRightRotation(Quaternionf rightRotation) {
      this.rightRotation = rightRotation;
      return this;
    }

    @Override
    public Builder setRange(float range) {
      this.range = range;
      return this;
    }

    @Override
    public Builder setBrightness(int block, int sky) {
      this.brightness = asNMSBrightness(block, sky);
      return this;
    }

    @Override
    public ModelData build() {
      return new ModelDataImpl(resourceKey, itemModel, translation,
          leftRotation, scale, rightRotation, range, brightness);
    }
  }

  @Nullable
  public static Brightness asNMSBrightness(int block, int sky) {
    return !(block < 0 || sky < 0) ? new Brightness(block, sky) : null;
  }
}
