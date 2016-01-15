package conversion7.spashole.game;

import conversion7.gdxg.core.AbstractClientGraphic;
import conversion7.gdxg.core.game_scene.GameScene;
import conversion7.gdxg.core.game_scene.camera.CameraController;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.artemis_odb.systems.CameraControllerFocusSystem;
import conversion7.spashole.game.artemis_odb.systems.EntityListenEnterInCameraViewSystem;
import conversion7.spashole.game.artemis_odb.systems.EntityListenExitFromCameraViewSystem;
import org.slf4j.Logger;

public class ClientGraphic extends AbstractClientGraphic {

    private static final Logger LOG = Utils.getLoggerForClass();

    public ClientGraphic(float viewportScale, int screenWidthInPx, int screenHeightInPx, CameraController cameraController) {
        super(viewportScale, screenWidthInPx, screenHeightInPx, cameraController, new ClientUi());
        SpasholeApp.graphic = this;
        SpasholeApp.ui = (ClientUi) clientUi;
        CameraControllerFocusSystem.setCameraController(getCameraController());
        EntityListenEnterInCameraViewSystem.setCameraController(getCameraController());
        EntityListenExitFromCameraViewSystem.setCameraController(getCameraController());
        LOG.info("Created.");
    }

    @Override
    public void resize(int screenWidth, int screenHeight) {
        super.resize(screenWidth, screenHeight);
    }

    @Override
    public void draw(GameScene activeStage, float delta) {
        super.draw(activeStage, delta);
    }
}
