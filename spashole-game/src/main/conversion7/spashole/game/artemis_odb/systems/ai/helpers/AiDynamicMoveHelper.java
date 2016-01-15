package conversion7.spashole.game.artemis_odb.systems.ai.helpers;

import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.artemis_odb.systems.MovementConstant;
import conversion7.spashole.game.artemis_odb.systems.MovementConstantManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;

public class AiDynamicMoveHelper {
    private static final Vector2 moveDirectionWip = new Vector2();
    public static final float DELTA_STEP = AiDynamicSystem.DELTA_STEP;

    public static void step(int entity) {
        Box2dBodyComponent attackerBody = Box2dBodySystem.components.get(entity);
        MovementConstant movementConstant = MovementConstantManager.components.get(entity).constant;
        MathUtilsExt.angleRadiansToVector(
                attackerBody.body.getAngle()
                , moveDirectionWip);
        moveDirectionWip.nor();
        Box2dBodySystem.move(attackerBody
                , moveDirectionWip
                , movementConstant.moveMltpl
                , DELTA_STEP
                , true);
    }
}
