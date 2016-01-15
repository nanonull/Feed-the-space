package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.ObjectMap;
import conversion7.gdxg.core.game_scene.camera.CameraController;
import conversion7.spashole.game.artemis_odb.systems.utils.ShotRunnable;

public class EntityListenExitFromCameraViewSystem extends IteratingSystem {
    public static ComponentMapper<EntityListenExitFromCameraViewComponent> components;
    private static CameraController cameraController;

    public EntityListenExitFromCameraViewSystem() {
        super(Aspect.all(EntityListenExitFromCameraViewComponent.class, Position2dComponent.class));
    }

    public static void setCameraController(CameraController cameraController) {
        EntityListenExitFromCameraViewSystem.cameraController = cameraController;
    }

    @Override
    protected void process(int entityId) {
        Position2dComponent position2dComponent = Position2dSystem.components.get(entityId);

        if (!cameraController.getCameraWorldViewRect().contains(position2dComponent.pos.x, position2dComponent.pos.y)) {
            components.remove(entityId);
            EntityListenEnterInCameraViewSystem.components.create(entityId);
            if (EntityExitsCameraViewTriggerManager.has(entityId)) {
                runTriggers(entityId);
            }
        }
    }

    private void runTriggers(int entityId) {
        EntityExitsCameraViewTriggerComponent entityExitsCameraViewTriggerComponent = EntityExitsCameraViewTriggerManager.get(entityId);
        ObjectMap.Entries<String, ShotRunnable> iterator = entityExitsCameraViewTriggerComponent.runnables.iterator();
        while (iterator.hasNext()) {
            ObjectMap.Entry<String, ShotRunnable> next = iterator.next();
            next.value.runnable.run();
            if (next.value.singleShot) {
                iterator.remove();
            }
        }
    }
}