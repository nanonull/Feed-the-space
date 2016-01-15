package conversion7.spashole.game.artemis_odb.systems.ai.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.utils.PointEffectHelper;
import conversion7.spashole.game.utils.SpasholeUtils;

public class AiWalkAroundHelper {
    public static final TestObstacles TEST_OBSTACLES = new TestObstacles();
    private static Vector2 moveDirectionWip = new Vector2();
    private static Vector2 start = new Vector2();
    private static Vector2 finish = new Vector2();
    private static float TEST_DISTANCE = SpasholeUtils.sceneToWorld(242f);
    private static float MOVE_STEP_DISTANCE = SpasholeUtils.sceneToWorld(42f);

    public static void step(int entityId) {
        Box2dBodyComponent bodyComponent = Box2dBodySystem.components.get(entityId);
        float bodyAngle = bodyComponent.body.getAngle();
        Vector2 bodyPos = bodyComponent.body.getPosition();

        start.set(bodyPos);
        MathUtilsExt.angleRadiansToVector(bodyAngle, moveDirectionWip);
        moveDirectionWip.nor();
        finish.set(bodyPos)
                .add(moveDirectionWip.x * TEST_DISTANCE, moveDirectionWip.y * TEST_DISTANCE);
        if (!DebugFlags.DO_NOT_SHOW_WALK_STEPS) {
            PointEffectHelper.createPoint(finish, Color.WHITE);
        }

        TEST_OBSTACLES.reset();
        SpasholeApp.box2dWorld.rayCast(TEST_OBSTACLES, start, finish);

        if (TEST_OBSTACLES.hasObstacles) {
            Box2dBodySystem.applyTorqueToRotateBy(
                    MathUtilsExt.toRadians(22),
                    bodyComponent.body);
        } else {
            Box2dBodySystem.applyTorqueToRotateBy(
                    MathUtilsExt.toRadians(3),
                    bodyComponent.body);
            Box2dBodySystem.applyForceToMoveBy(
                    moveDirectionWip.x * MOVE_STEP_DISTANCE
                    , moveDirectionWip.y * MOVE_STEP_DISTANCE
                    , bodyComponent.body);
        }
    }

    static class TestObstacles implements RayCastCallback {
        public boolean hasObstacles;


        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            hasObstacles = true;
            return 0;
        }

        public void reset() {
            hasObstacles = false;
        }
    }
}
