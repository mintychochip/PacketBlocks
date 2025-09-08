//package org.aincraft;
//
//import org.aincraft.api.ClientPacketBlock;
//import org.aincraft.api.ClientPacketBlock.Record;
//import org.aincraft.api.ClientBlockData;
//import org.bukkit.Chunk;
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.plugin.Plugin;
//import org.joml.Quaternionf;
//import org.joml.Vector3f;
//
//public class ClientPacketBlockMapper {
//
//  private final Plugin plugin;
//
//  public ClientPacketBlockMapper(Plugin plugin) {
//    this.plugin = plugin;
//  }
//
//  public ClientPacketBlock.Record asRecord(ClientPacketBlock packetBlock) {
//    Location location = packetBlock.getLocation();
//    World world = location.getWorld();
//    Chunk chunk = location.getChunk();
//    ClientBlockData blockData = packetBlock.blockData();
//    String itemModel = blockData.itemModel().toString();
//    Vector3f scale = blockData.scale();
//    Vector3f translation = blockData.translation();
//    Quaternionf leftRotation = blockData.leftRotation();
//    Quaternionf rightRotation = blockData.rightRotation();
//    return new Record(world.getName(), location.x(), location.y(), location.z(), chunk.getX(),
//        chunk.getZ(), itemModel, blockData.blockLight(), blockData.skyLight(), translation.x(),
//        translation.y(), translation.z(), leftRotation.x(), leftRotation.y(), leftRotation.z(),
//        leftRotation.w(), scale.x(), scale.y(), scale.z(), rightRotation.x(), rightRotation.y(),
//        rightRotation.z(), rightRotation.w());
//  }
////
////  public ClientPacketBlock domain(ClientPacketBlock.Record blockData) throws IllegalArgumentException {
////    Material material = Material.valueOf(blockData.material());
////    Key itemModel = NamespacedKey.fromString(blockData.itemModel());
////    World world = Bukkit.getWorld(blockData.world());
////    if (world == null) {
////      throw new IllegalArgumentException("failed to find a valid world");
////    }
////    CraftWorld craftWorld = (CraftWorld) world;
////    ServerLevel serverLevel = craftWorld.getHandle();
////    Vec3 position = new Vec3(blockData.x(), blockData.y(), blockData.z());
////    int block = blockData.block();
////    int sky = blockData.sky();
////    Vector3f translation = new Vector3f(blockData.tx(), blockData.ty(), blockData.tz());
////    Quaternionf leftRotation = new Quaternionf(blockData.lx(), blockData.ly(), blockData.lz(), blockData.lw());
////    Vector3f scale = new Vector3f(blockData.sx(), blockData.sy(), blockData.sz());
////    Quaternionf rightRotation = new Quaternionf(blockData.rx(), blockData.ry(), blockData.rz(), blockData.rw());
////    ClientBlock blockEntity = ClientBlockImpl.builder(world, plugin)
////        .setItemModel(itemModel)
////        .setTranslation(translation)
////        .setLeftRotation(leftRotation).setScale(scale).setRightRotation(rightRotation).build();
////    return new ClientPacketBlockImpl(blockEntity)
////  }
//}
