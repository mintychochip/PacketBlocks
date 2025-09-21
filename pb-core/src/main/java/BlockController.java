import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockController implements Listener {

  private final ItemService itemService;

  @Inject
  public BlockController(ItemService itemService) {
    this.itemService = itemService;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  private void onBlockPlace(final BlockPlaceEvent event) {
    ItemStack itemInHand = event.getItemInHand();
    itemService.
  }
}
