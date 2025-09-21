import com.google.inject.Inject;
import org.aincraft.EntityModelAttributes;
import org.aincraft.EntityModelData;
import org.aincraft.config.ConfigurationFactory;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityModelDataFactoryImpl implements ConfigurationFactory<EntityModelData> {

  private final EntityModelData.Factory factory;

  @Inject
  public EntityModelDataFactoryImpl(EntityModelData.Factory factory) {
    this.factory = factory;
  }

  @Override
  public @NotNull EntityModelData create(@Nullable ConfigurationSection section)
      throws IllegalArgumentException {
    EntityModelData modelData = factory.create();
    if (section.contains("item-model")) {
      modelData.setAttribute(EntityModelAttributes.ITEM_MODEL, NamespacedKey.fromString(section.getString("item-model")));
    }
    return modelData;
  }
}
