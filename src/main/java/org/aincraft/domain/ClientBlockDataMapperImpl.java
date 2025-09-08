package org.aincraft.domain;

import net.kyori.adventure.key.Key;
import org.aincraft.Mapper;
import org.aincraft.api.ClientBlockData;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

final class ClientBlockDataMapperImpl implements
    Mapper<ClientBlockData, ClientBlockData.Record> {

  @Override
  public @NotNull ClientBlockData asDomain(ClientBlockData.@NotNull Record record) {
    Key resourceKey = NamespacedKey.fromString(record.resourceKey());
    Key itemModel = NamespacedKey.fromString(record.itemModel());
    Vector3f translation = new Vector3f(record.tx(), record.ty(), record.tz());
    Quaternionf leftRotation = new Quaternionf(record.lx(), record.ly(), record.lz(), record.lw());
    Vector3f scale = new Vector3f(record.sx(), record.sy(), record.sz());
    Quaternionf rightRotation = new Quaternionf(record.rx(), record.ry(), record.rz(), record.rw());
    return new ClientBlockDataImpl(resourceKey, itemModel, translation,
        leftRotation, scale,
        rightRotation, record.range(),
        ClientBlockDataImpl.asNMSBrightness(record.block(), record.sky()));
  }

  @Override
  public @NotNull ClientBlockData.Record asRecord(@NotNull ClientBlockData domain) {
    Vector3f translation = domain.translation();
    Quaternionf leftRotation = domain.leftRotation();
    Vector3f scale = domain.scale();
    Quaternionf rightRotation = domain.rightRotation();
    return new ClientBlockData.Record(domain.resourceKey().toString(),
        domain.itemModel().toString(), translation.x(),
        translation.y(), translation.z(), leftRotation.x(), leftRotation.y(), leftRotation.z(),
        leftRotation.w(), scale.x(), scale.y(), scale.z(), rightRotation.x(), rightRotation.y(),
        rightRotation.z(), rightRotation.w(), domain.range(), domain.blockLight(),
        domain.skyLight());
  }
}
