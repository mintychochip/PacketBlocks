package org.aincraft.domain;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.aincraft.config.YamlConfiguration;

public final class ItemDataRepositoryProviderImpl implements
    Provider<Repository<String, ItemData.Record>> {

  private final YamlConfiguration yamlConfiguration;

  @Inject
  public ItemDataRepositoryProviderImpl(YamlConfiguration yamlConfiguration) {
    this.yamlConfiguration = yamlConfiguration;
  }

  @Override
  public Repository<String, Record> get() {
    return null;
  }
}
