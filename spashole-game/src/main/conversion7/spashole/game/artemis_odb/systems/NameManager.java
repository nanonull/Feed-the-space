package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Manager;

public class NameManager extends Manager {

    public static ComponentMapper<NameComponent> components;

    public static String getSysName(int entityId) {
        if (components.has(entityId)) {
            return components.get(entityId).name + " entity-" + entityId;
        }
        return "unnamed entity-" + entityId;
    }

    public static String getName(int entityId) {
        if (components.has(entityId)) {
            return components.get(entityId).name;
        }
        return "unnamed";
    }
}
