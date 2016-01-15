package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.ObjectMap;
import conversion7.gdxg.core.game_scene.camera.CameraController;
import conversion7.spashole.game.artemis_odb.systems.utils.ShotRunnable;

public class EntityListenEnterInCameraViewSystem extends IteratingSystem {
    public static ComponentMapper<EntityListenEnterInCameraViewComponent> components;
    private static CameraController cameraController;

    public EntityListenEnterInCameraViewSystem() {
        super(Aspect.all(EntityListenEnterInCameraViewComponent.class, Position2dComponent.class));
    }

    public static void setCameraController(CameraController cameraController) {
        EntityListenEnterInCameraViewSystem.cameraController = cameraController;
    }

    @Override
    protected void process(int entityId) {
        Position2dComponent position2dComponent = Position2dSystem.components.get(entityId);

        if (cameraController.getCameraWorldViewRect().contains(position2dComponent.pos.x, position2dComponent.pos.y)) {
            components.remove(entityId);
            EntityListenExitFromCameraViewSystem.components.create(entityId);
            if (EntityEntersCameraViewTriggerManager.has(entityId)) {
                runTriggers(entityId);
            }
        }
    }

    private void runTriggers(int entityId) {
        EntityEntersCameraViewTriggerComponent entityEntersCameraViewTriggerComponent = EntityEntersCameraViewTriggerManager.get(entityId);
        ObjectMap.Entries<String, ShotRunnable> iterator = entityEntersCameraViewTriggerComponent.runnables.iterator();
        while (iterator.hasNext()) {
            ObjectMap.Entry<String, ShotRunnable> next = iterator.next();
            next.value.runnable.run();
            if (next.value.singleShot) {
                iterator.remove();
            }
        }
    }
}