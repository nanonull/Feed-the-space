package conversion7.spashole.tests

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import conversion7.gdxg.core.CommonConstants
import conversion7.gdxg.core.utils.Utils
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.artemis_odb.systems.CameraControllerFocusSystem
import conversion7.spashole.tests.steps.CoreSteps
import org.slf4j.Logger
import spock.lang.Ignore
import spock.lang.Specification

class CameraTests extends Specification {
    private static final Logger LOG = Utils.getLoggerForClass();
    CoreSteps coreSteps = new CoreSteps()

    void 'test camera viewport'() {
        given:
        coreSteps.createClientCore()

        when:
        Camera camera = SpasholeApp.graphic.getCameraController().camera

        then:
        assert camera.viewportWidth == (int) SpasholeApp.graphic.scaleByViewPort(CommonConstants.SCREEN_WIDTH_IN_PX)
        assert camera.viewportHeight == (int) SpasholeApp.graphic.scaleByViewPort(CommonConstants.SCREEN_HEIGHT_IN_PX)
    }

    /**Result: from -50 to 50*/
    void 'print camera frustrum'() {
        given:
        coreSteps.createClientCore()

        when:
        Camera camera = SpasholeApp.graphic.getCameraController().camera
        camera.position.setZero()

        then:
        def vector3 = new Vector3()
        LOG.info("X UP from {}", vector3)
        for (int i = 0; i < 10000; i++) {
            vector3.x = i
            if (!camera.frustum.pointInFrustum(vector3)) {
                LOG.info("not-in-frustrum from {}", vector3)
                break
            }
        }

        LOG.info("X DOWN from {}", vector3)
        for (int i = 0; i > -10000; i--) {
            vector3.x = i
            if (!camera.frustum.pointInFrustum(vector3)) {
                LOG.info("not-in-frustrum from {}", vector3)
                break
            }
        }
    }

    @Ignore
    void 'test camera contains point'() {
        given:
        coreSteps.createClientCore()
        // TODO freeze camera controller
        SpasholeApp.ARTEMIS_ENGINE.getSystem(CameraControllerFocusSystem.class).enabled = false

        when:
        def cameraController = SpasholeApp.graphic.getCameraController()
        Camera camera = cameraController.camera
        camera.position.set(0, 0, 0)
        coreSteps.waitForNextRenderFrame()

        then:
        LOG.info('' + camera.viewportWidth)
        LOG.info('' + camera.viewportHeight)
        assert cameraController.isWorldPointVisible(new Vector2(0, 0))
        assert cameraController.isWorldPointVisible(new Vector2(-51, 0))
        assert cameraController.isWorldPointVisible(new Vector2(51, 0))
        assert !cameraController.isWorldPointVisible(new Vector2(-52, 0))
        assert !cameraController.isWorldPointVisible(new Vector2(52, 0))

        cleanup:
        SpasholeApp.ARTEMIS_ENGINE.getSystem(CameraControllerFocusSystem.class).enabled = true
    }
}
