package org.aincraft.domain;

import com.google.inject.Inject;
import org.aincraft.BlockBindingImpl;
import org.aincraft.Mapper;
import org.aincraft.api.BlockBinding;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public class BlockBindingFactoryImpl implements BlockBindingFactory {

  private final Repository<String, ModelData.Record> modelDataRepository;
  private final Mapper<ModelData, Record> modelDataMapper;

  @Inject
  public BlockBindingFactoryImpl(Repository<String, ModelData.Record> modelDataRepository,
      Mapper<ModelData, Record> modelDataMapper) {
    this.modelDataRepository = modelDataRepository;
    this.modelDataMapper = modelDataMapper;
  }

  @Override
  public BlockBinding bind(Location location, String resourceKey) {
    Record load = modelDataRepository.load(resourceKey);
    return new BlockBindingImpl(NamespacedKey.fromString(resourceKey),modelDataMapper.asDomain(load),location);
  }
}
