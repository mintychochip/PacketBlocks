package org.aincraft;

import com.google.inject.Inject;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.aincraft.EntityModelImpl.EntityModelDataImpl;
import org.aincraft.PacketBlock.PacketBlockMeta;
import org.aincraft.registry.RegistryAccess;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

record BridgeImpl(RegistryAccess registryAccess, ItemService itemService) implements Bridge {

  @Inject
  BridgeImpl {
  }

  @Override
  public @NotNull PacketBlockFactory packetBlockFactory() {
    return new PacketBlockFactory() {
      @Override
      public EntityModel create(EntityType entityType, Location location) {
        return EntityModelImpl.create(entityType, location.getWorld(), location.toVector());
      }

      @Override
      public EntityModelData create() {
        return EntityModelDataImpl.create();
      }

      @Override
      public PacketBlockMeta createBlockMeta(Key key, BlockItemMeta blockItemMeta,
          EntityModelData entityModelData, Map<SoundType, SoundEntry> entries) {
        return new PacketBlockMetaImpl(key, blockItemMeta, entityModelData, entries);
      }
    };
  }
}
