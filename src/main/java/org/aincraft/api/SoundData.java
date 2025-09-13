package org.aincraft.api;

import java.util.Map;
import org.aincraft.api.Builder.Buildable;
import org.aincraft.api.SoundData.SoundDataBuilder;
import org.aincraft.api.SoundDataImpl.BuilderImpl;
import org.jetbrains.annotations.Nullable;

public interface SoundData extends Buildable<SoundDataBuilder, SoundData> {

  static SoundDataBuilder builder() {
    return new BuilderImpl();
  }

  enum SoundType {
    BREAK,
    PLACE
  }

  /** Full map of configured sounds. */
  Map<SoundType, SoundEntry> entries();

  /** Look up a single sound; may be absent. */
  @Nullable
  SoundEntry entry(SoundType type);

  interface SoundDataBuilder extends org.aincraft.api.Builder<SoundData> {

    SoundDataBuilder entries(Map<SoundType, SoundEntry> entries);

    SoundDataBuilder entry(SoundType type, SoundEntry entry);
  }
}
