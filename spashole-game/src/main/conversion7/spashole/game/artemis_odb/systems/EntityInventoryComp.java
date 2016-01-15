package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityInventoryComp extends Component {
    protected ObjectMap<InventoryItemComp.Type, Integer> items = new ObjectMap<>();
    private ObjectMap<InventoryItemComp.Type, Runnable> itemTriggers = new ObjectMap<>();

    public void addItem(InventoryItemComp.Type type) {
        Integer qty = items.get(type);
        if (qty == null) {
            qty = 1;
        } else {
            qty++;
        }
        items.put(type, qty);

        Runnable runnable = itemTriggers.get(type);
        if (runnable != null) {
            runnable.run();
            itemTriggers.remove(type);
        }
    }

    public void removeItem(InventoryItemComp.Type type) {
        Integer qty = items.get(type);
        if (qty == null) {
            return;
        }

        qty--;
        if (qty <= 0) {
            items.remove(type);
        } else {
            items.put(type, qty);
        }
    }

    public void addItemTrigger(InventoryItemComp.Type type, Runnable trigger) {
        itemTriggers.put(type, trigger);
    }
}
