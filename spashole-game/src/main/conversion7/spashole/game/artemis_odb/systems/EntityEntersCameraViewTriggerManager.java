package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Manager;

public class EntityEntersCameraViewTriggerManager  extends Manager {

    private static ComponentMapper<EntityEntersCameraViewTriggerComponent> components;

    public static EntityEntersCameraViewTriggerComponent componentCreate(int entityId) {
        EntityListenEnterInCameraViewSystem.components.create(entityId);
        return components.create(entityId);
    }

    public static boolean has(int entityId) {
        return components.has(entityId);
    }

    public static EntityEntersCameraViewTriggerComponent get(int entityId) {
        return components.get(entityId);
    }
}