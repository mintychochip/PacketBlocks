package org.aincraft.domain.yaml;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import org.aincraft.config.ConfigurationFactory;
import org.aincraft.loot.Loot;
import org.aincraft.loot.LootData;
import org.aincraft.loot.LootData.LootDataBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LootDataFactoryImpl implements ConfigurationFactory<LootData> {

  private static final LootData DEFAULT = LootData.builder().dropOnShear(false)
      .dropOnSilkTouch(true).build();

  @Override
  public @NotNull LootData create(@Nullable ConfigurationSection section)
      throws IllegalArgumentException {
    Preconditions.checkArgument(section != null);
    LootDataBuilder builder = DEFAULT.toBuilder();
    if (section.contains("drop-on-silk-touch")) {
      builder.dropOnSilkTouch(section.getBoolean("drop-on-silk-touch"));
    }
    if (section.contains("drop-on-shear")) {
      builder.dropOnShear(section.getBoolean("drop-on-shear"));
    }
    if (section.contains("loot")) {
      for (Map<?, ?> loot : section.getMapList("loot")) {
        String type = Optional.ofNullable((String) loot.get("type")).orElse("unknown");
        int min = Optional.ofNullable(asInt(loot.get("min"))).orElse(1);
        int max = Optional.ofNullable(asInt(loot.get("max"))).orElse(1);
        double chance = Optional.ofNullable(asDouble(loot.get("chance"))).orElse(1.0);
        switch (type.toLowerCase(Locale.ENGLISH)) {
          case "item" -> {
            String keyString = (String) loot.get("key");
            Key itemKey = NamespacedKey.fromString(keyString);
            builder.addLoot(Loot.item(min, max, itemKey), chance);
          }
          case "experience" -> {
            builder.addLoot(Loot.experience(min, max), chance);
          }
          default -> {
            throw new IllegalArgumentException("unknown loot type:" + type);
          }
        }
      }
    }
    return builder.build();
  }

  private static Integer asInt(Object o) {
    if (o == null) {
      return null;
    }
    if (o instanceof Number n) {
      return n.intValue();
    }
    throw new IllegalArgumentException("Expected number, got: " + o);
  }

  private static Double asDouble(Object o) {
    if (o == null) {
      return null;
    }
    if (o instanceof Number n) {
      return n.doubleValue();
    }
    throw new IllegalArgumentException("Expected number, got: " + o);
  }
}
