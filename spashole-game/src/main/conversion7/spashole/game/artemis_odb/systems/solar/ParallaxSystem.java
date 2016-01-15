package conversion7.spashole.game.artemis_odb.systems.solar;

import com.artemis.BaseSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import conversion7.gdxg.core.game_scene.camera.CameraController;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;
import org.testng.Assert;

public class ParallaxSystem extends BaseSystem {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static final float PARALLAX_STEP_SCENE_SIZE = 512;
    public static final float PARALLAX_STEP_WORLD_SIZE = SpasholeUtils.sceneToWorld(PARALLAX_STEP_SCENE_SIZE);
    static Image image1;
    static Group group;
    private static CameraController cameraController;

    public static void init(Stage stage2d, Vector2 _shipBodyPos, CameraController cameraController) {
        LOG.info("PARALLAX_STEP_SCENE_SIZE {}", PARALLAX_STEP_SCENE_SIZE);
        LOG.info("PARALLAX_STEP_WORLD_SIZE {}", PARALLAX_STEP_WORLD_SIZE);

        ParallaxSystem.cameraController = cameraController;
        group = new Group();
        stage2d.addActor(group);
//        group.setPosition(_shipBodyPos.x, _shipBodyPos.y);
        group.setScale(SpasholeApp.WORLD_SCALE);

        image1 = new Image(SpasholeAssets.space1);
        Assert.assertEquals(image1.getWidth(), PARALLAX_STEP_SCENE_SIZE);
        Assert.assertEquals(image1.getHeight(), PARALLAX_STEP_SCENE_SIZE);
        group.addActor(image1);
        image1.setSize(image1.getWidth() * 7, image1.getWidth() * 7);
        Actor2dManager.adjustPositionCenterBySize(image1);

    }

    @Override
    protected void processSystem() {
        group.setZIndex(0);
        Vector3 camPos = cameraController.getCamera().position;
        int parX = (int) (camPos.x / PARALLAX_STEP_WORLD_SIZE) + 1;
        int parY = (int) (camPos.y / PARALLAX_STEP_WORLD_SIZE) + 1;
        if (LOG.isDebugEnabled()) {
            LOG.debug(camPos.toString());
            LOG.debug(String.format("%s %s", parX, parY));
            LOG.debug(String.format("%s %s", parX, parY));
            LOG.debug("");
        }

        group.setPosition(
                PARALLAX_STEP_WORLD_SIZE * parX
                , PARALLAX_STEP_WORLD_SIZE * parY
        );
    }
}