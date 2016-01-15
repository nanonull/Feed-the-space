package conversion7.spashole.game.artemis_odb.systems.ai.helpers;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.artemis_odb.systems.gun.GunComponent;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.utils.SpasholeUtils;

public class AiDynamicAttackHelper {

    public static final float CHASE_FROM_DISTANCE = SpasholeUtils.sceneToWorld(300);
    public static final float TARGET_ALMOST_GET_FROM_ANGLE = MathUtilsExt.toRadians(15);
    public static final float TARGET_GET_FROM_ANGLE = TARGET_ALMOST_GET_FROM_ANGLE / 4;
    private static final Vector2 posDiff = new Vector2();

    public static void step(Entity attackerE, int targetE) {
        boolean targetAngleReached = AiDynamicRotateOnTargetHelper
                .step(attackerE.getId(), targetE, posDiff);

        if (targetAngleReached) {
            GunComponent gunComponent = GunSystem.components.get(attackerE);
            gunComponent.makeShot();
        }

        if (posDiff.len() > CHASE_FROM_DISTANCE) {
            AiDynamicMoveHelper.step(attackerE.getId());
        }
    }
}