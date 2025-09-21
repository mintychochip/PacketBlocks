import net.kyori.adventure.key.Key;
import org.aincraft.BlockItemMeta;
import org.aincraft.EntityModelData;
import org.aincraft.PacketBlock.PacketBlockMeta;

public class PacketBlockMetaImpl implements PacketBlockMeta {

  private final String resourceKey;
  private final Key itemModel;
  private final Soundentry

  public PacketBlockMetaImpl(Key itemModel) {
    this.resourceKey = resourceKey;
    this.itemModel = itemModel;
  }

  @Override
  public String resourceKey() {
    return resourceKey;
  }

  @Override
  public BlockItemMeta getBlockItemMeta() {
    return null;
  }

  @Override
  public EntityModelData entityModelMeta() {
    return null;
  }
}
