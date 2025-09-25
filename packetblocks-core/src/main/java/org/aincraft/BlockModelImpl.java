package org.aincraft;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;

final class BlockModelImpl implements BlockModel {

  private final EntityModel entityModel;

  public BlockModelImpl(EntityModel entityModel) {
    this.entityModel = entityModel;
  }

  @Override
  public boolean visible(Player player) {
    return entityModel.visible(player);
  }

  @Override
  public void show(Player player) {
    entityModel.show(player);
  }

  @Override
  public void hide(Player player) {
    entityModel.hide(player);
  }

  @Override
  public void move(Location location) {
    entityModel.move(location);
  }

  @Override
  public void teleport(Location location) {
    entityModel.teleport(location);
  }

  @Override
  public Set<Player> viewers() {
    return entityModel.viewers();
  }

  @Override
  public void data(BlockModelData blockModelData) {
    if (blockModelData instanceof BlockModelDataImpl impl) {
      entityModel.setData(impl.entityModelData());
    }
  }

  @Override
  public BlockModelData data() {
    return new BlockModelDataImpl(entityModel.getData());
  }
}
