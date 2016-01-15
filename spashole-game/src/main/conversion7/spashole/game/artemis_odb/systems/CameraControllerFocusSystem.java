package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.game_scene.camera.CameraController;
import conversion7.gdxg.core.dialog.view.DialogWindow;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.utils.SpasholeUtils;

import java.util.UUID;

import static conversion7.gdxg.core.utils.MathUtilsExt.simpleMax;
import static conversion7.gdxg.core.utils.MathUtilsExt.simpleMin;

public class CameraControllerFocusSystem extends BaseSystem {

    private static final int VIEW_DISTANCE_DEFAULT = (int) SpasholeUtils.sceneToWorld(400f);
    private static UUID targetEntityUuid;
    private static float viewCenterDistance;
    private static CameraController cameraController;
    private static Mode mode = Mode.FULL_SCREEN;
    Vector2 expectedViewPoint = new Vector2();
    Vector2 cameraStepWip = new Vector2();
    Vector2 cameraViewCenter = new Vector2();
    Vector2 expectedViewCenter = new Vector2();
    Rectangle expectedViewRectangle = new Rectangle();

    public static UUID getTargetEntityUuid() {
        return targetEntityUuid;
    }

    public static CameraController getCameraController() {
        return cameraController;
    }

    public static void setCameraController(CameraController cameraController) {
        CameraControllerFocusSystem.cameraController = cameraController;
    }

    public static void setTarget(UUID entityUuid) {
        setTarget(entityUuid, VIEW_DISTANCE_DEFAULT);
    }

    public static void setMode(Mode mode) {
        CameraControllerFocusSystem.mode = mode;
    }

    public static void setTarget(UUID entityUuid, float viewCenterDistance) {
        CameraControllerFocusSystem.targetEntityUuid = entityUuid;
        CameraControllerFocusSystem.viewCenterDistance = viewCenterDistance;

        Entity entity = SpasholeApp.ARTEMIS_UUIDS.getEntity(targetEntityUuid);
        if (entity != null) {
            Rotation2dComponent rotation2dComponent = Rotation2dSystem.components.getSafe(entity);
            if (rotation2dComponent == null) {
                CameraControllerFocusSystem.viewCenterDistance = 0;
            }
        }
    }

    @Override
    protected void processSystem() {
        Entity entity = SpasholeApp.ARTEMIS_UUIDS.getEntity(targetEntityUuid);
        if (entity == null) {
            return;
        }

        Position2dComponent position2dComponent = Position2dSystem.components.get(entity);
        expectedViewPoint.set(position2dComponent.pos.x, position2dComponent.pos.y);

        if (viewCenterDistance > 0) {
            Rotation2dComponent rotation2dComponent = Rotation2dSystem.components.get(entity);
            float angleRads = MathUtils.degreesToRadians * rotation2dComponent.angleDegrees;
            expectedViewPoint.add(
                    viewCenterDistance * MathUtils.cos(angleRads)
                    , viewCenterDistance * MathUtils.sin(angleRads)
            );
        }

        float minX = simpleMin(position2dComponent.pos.x, expectedViewPoint.x);
        float minY = simpleMin(position2dComponent.pos.y, expectedViewPoint.y);
        float maxX = simpleMax(position2dComponent.pos.x, expectedViewPoint.x);
        float maxY = simpleMax(position2dComponent.pos.y, expectedViewPoint.y);
        expectedViewRectangle.set(minX, minY, maxX - minX, maxY - minY);

        // get centers of areas
        expectedViewRectangle.getCenter(expectedViewCenter);
        cameraController.getCameraWorldViewRect().getCenter(cameraViewCenter);
        if (mode == Mode.QUEST_VIEW) {
            cameraViewCenter.x += SpasholeUtils.sceneToWorld(DialogWindow.WINDOW_WIDTH_WITHOUT_PICTURE_VIEW) / 2f;
        }

        cameraStepWip.set(expectedViewCenter).sub(cameraViewCenter);
        cameraStepWip.scl(0.055f);
        cameraController.getCamera().position.add(cameraStepWip.x, cameraStepWip.y, 0);
    }

    public enum Mode {
        FULL_SCREEN, QUEST_VIEW
    }
}
