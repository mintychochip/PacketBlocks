import java.util.List;
import org.aincraft.BlockBinding;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface BlockBindingRepository {

  @Nullable
  BlockBinding load(Location location);

  boolean delete(Location location);

  boolean save(BlockBinding blockBinding);

  List<BlockBinding> loadAllByChunk(Chunk chunk);
}
