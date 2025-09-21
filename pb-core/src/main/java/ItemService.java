import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

@Internal
public interface ItemService {

  Optional<String> read(ItemStack itemStack);
  void write(ItemStack itemStack, String resourceKey);
}
