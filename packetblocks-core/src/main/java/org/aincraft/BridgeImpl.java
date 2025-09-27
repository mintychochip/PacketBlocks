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
      public BlockModel create(Location location, BlockModelData blockModelData) {
        EntityModel entityModel = EntityModelImpl.create(EntityType.ITEM_DISPLAY,
            location.getWorld(), location.toVector());
        if (blockModelData instanceof BlockModelDataImpl impl) {
          entityModel.setData(impl.entityModelData());
        }
        return new BlockModelImpl(entityModel, blockModelData);
      }

      @Override
      public Builder blockModelDataBuilder(Key key) {
        return new BlockModelDataImpl.BuilderImpl(key);
      }


      @Override
      public PacketBlockMeta createBlockMeta(Key key, BlockItemMeta blockItemMeta,
          BlockModelData blockModelData, Map<SoundType, SoundEntry> entries) {
        return new PacketBlockMetaImpl(key, blockItemMeta, blockModelData, entries);
      }
    };
  }
}
