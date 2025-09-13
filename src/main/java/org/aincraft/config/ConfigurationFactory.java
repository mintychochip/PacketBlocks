package org.aincraft.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigurationFactory<T> {

  @NotNull
  T create(@Nullable ConfigurationSection section) throws IllegalArgumentException;
}
