//package org.aincraft.domain;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//import org.aincraft.ConnectionSource;
//import org.aincraft.api.ClientBlockData;
//import org.aincraft.api.ClientPacketBlock;
//import org.aincraft.api.ClientPacketBlock.Record;
//import org.bukkit.Chunk;
//
//public class PacketBlockRepositoryImpl implements PacketBlockRepository {
//
//  private final ConnectionSource connectionSource;
//
//  public PacketBlockRepositoryImpl(ConnectionSource connectionSource) {
//    this.connectionSource = connectionSource;
//  }
//
//  @Override
//  public boolean upsert(Record blockData) {
//    String sql = """
//        INSERT INTO packet_blocks (
//            world, x, y, z, cx, cz, item_model, block, sky,
//            tx, ty, tz,
//            lx, ly, lz, lw,
//            sx, sy, sz,
//            rx, ry, rz, rw
//        )
//        VALUES (?,?,?,?,?,?,?,?,?,
//                ?,?,?, ?,?,?,?,
//                ?,?,?, ?,?,?,?)
//        ON CONFLICT (world, x, y, z) DO UPDATE SET
//            cx = EXCLUDED.cx,
//            cz = EXCLUDED.cz,
//            item_model = EXCLUDED.item_model,
//            block = EXCLUDED.block,
//            sky = EXCLUDED.sky,
//            tx = EXCLUDED.tx,
//            ty = EXCLUDED.ty,
//            tz = EXCLUDED.tz,
//            lx = EXCLUDED.lx,
//            ly = EXCLUDED.ly,
//            lz = EXCLUDED.lz,
//            lw = EXCLUDED.lw,
//            sx = EXCLUDED.sx,
//            sy = EXCLUDED.sy,
//            sz = EXCLUDED.sz,
//            rx = EXCLUDED.rx,
//            ry = EXCLUDED.ry,
//            rz = EXCLUDED.rz,
//            rw = EXCLUDED.rw
//        """;
//
//    try (Connection connection = connectionSource.getConnection();
//        PreparedStatement ps = connection.prepareStatement(sql)) {
//
//      ps.setString(1, blockData.world());
//      ps.setDouble(2, blockData.x());
//      ps.setDouble(3, blockData.y());
//      ps.setDouble(4, blockData.z());
//      ps.setInt(5, blockData.cx());
//      ps.setInt(6, blockData.cz());
//      ClientBlockData.Record dataRecord = blockData.dataRecord();
//      ps.setString(7, dataRecord.itemModel());
//      ps.setInt(8, dataRecord.block());
//      ps.setInt(9, dataRecord.sky());
//      ps.setFloat(10, dataRecord.tx());
//      ps.setFloat(11, dataRecord.ty());
//      ps.setFloat(12, dataRecord.tz());
//      ps.setFloat(13, dataRecord.lx());
//      ps.setFloat(14, dataRecord.ly());
//      ps.setFloat(15, dataRecord.lz());
//      ps.setFloat(16, dataRecord.lw());
//      ps.setFloat(17, dataRecord.sx());
//      ps.setFloat(18, dataRecord.sy());
//      ps.setFloat(19, dataRecord.sz());
//      ps.setFloat(20, dataRecord.rx());
//      ps.setFloat(21, dataRecord.ry());
//      ps.setFloat(22, dataRecord.rz());
//      ps.setFloat(23, dataRecord.rw());
//      return ps.executeUpdate() > 0;
//    } catch (SQLException e) {
//      throw new RuntimeException("Failed to upsert packet_blocks blockData", e);
//    }
//  }
//
//
//  @Override
//  public List<ClientPacketBlock> loadBlocks(Chunk chunk) {
//    return List.of();
//  }
//}
