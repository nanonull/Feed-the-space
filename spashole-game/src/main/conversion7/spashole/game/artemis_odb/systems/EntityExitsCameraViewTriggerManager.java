package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Manager;

public class EntityExitsCameraViewTriggerManager extends Manager {
    private static ComponentMapper<EntityExitsCameraViewTriggerComponent> components;

    public static EntityExitsCameraViewTriggerComponent componentCreate(int entityId) {
        components.create(entityId);
        return components.create(entityId);
    }

    public static boolean has(int entityId) {
        return components.has(entityId);
    }

    public static EntityExitsCameraViewTriggerComponent get(int entityId) {
        return components.get(entityId);
    }
}