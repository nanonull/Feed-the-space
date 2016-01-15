package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Manager;

public class EntityInventoryManager extends Manager {
    public static ComponentMapper<EntityInventoryComp> components;

    public static boolean has(InventoryItemComp.Type type, int entId) {
        EntityInventoryComp inventoryComp = components.getSafe(entId);
        return inventoryComp != null && inventoryComp.items.containsKey(type);
    }
}