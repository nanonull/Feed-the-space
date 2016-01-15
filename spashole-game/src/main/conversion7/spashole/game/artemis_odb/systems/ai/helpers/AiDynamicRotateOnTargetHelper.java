package conversion7.spashole.game.artemis_odb.systems.ai.helpers;

import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.artemis_odb.systems.MovementConstant;
import conversion7.spashole.game.artemis_odb.systems.MovementConstantManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;

public class AiDynamicRotateOnTargetHelper {

    public static final float DELTA_STEP = AiDynamicSystem.DELTA_STEP;

    /** Return true if target angle reached */
    public static boolean step(int entity, int targetE, Vector2 posDiff) {
        Position2dComponent attackerPos = Position2dSystem.components.get(entity);
        Position2dComponent targetPos = Position2dSystem.components.get(targetE);
        posDiff.set(targetPos.pos).sub(attackerPos.pos);

        float angleRadToTarget = MathUtilsExt.absRadians(posDiff.angleRad());
        Box2dBodyComponent attackerBody = Box2dBodySystem.components.get(entity);
        float attackerAngle = MathUtilsExt.absRadians(attackerBody.body.getAngle());

        float angleDiff = MathUtilsExt.diffAbsoluteRadians(attackerAngle, angleRadToTarget);
        float angleDiffAbs = Math.abs(angleDiff);
        if (angleDiffAbs <= AiDynamicAttackHelper.TARGET_GET_FROM_ANGLE) {
            return true;
        }

        MovementConstant movementConstant = MovementConstantManager.components.get(entity).constant;
        float torque;
        if (angleDiff < 0) {
            torque = -movementConstant.torqueForce;
        } else {
            torque = movementConstant.torqueForce;
        }
        Box2dBodySystem.rotate(attackerBody
                , torque
                , DELTA_STEP);

        if (angleDiffAbs <= AiDynamicAttackHelper.TARGET_ALMOST_GET_FROM_ANGLE) {
            return true;
        } else {
            return false;
        }

    }
}
