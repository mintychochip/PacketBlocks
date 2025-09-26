package org.aincraft;

import com.google.inject.Inject;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.aincraft.BlockModelData.Builder;
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
      public BlockModel create(Location location) {
        EntityModel entityModel = EntityModelImpl.create(EntityType.ITEM_DISPLAY,
            location.getWorld(), location.toVector());
        return new BlockModelImpl(entityModel);
      }

      @Override
      public Builder dataBuilder() {
        return new BlockModelDataImpl.BuilderImpl();
      }


      @Override
      public PacketBlockMeta createBlockMeta(Key key, BlockItemMeta blockItemMeta,
          BlockModelData blockModelData, Map<SoundType, SoundEntry> entries) {
        return new PacketBlockMetaImpl(key, blockItemMeta, blockModelData, entries);
      }
    };
  }
}
