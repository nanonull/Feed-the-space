package conversion7.spashole.game.artemis_odb.systems.ai.helpers;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunComponent;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.artemis_odb.systems.gun.StationaryGunComponent;
import conversion7.spashole.game.artemis_odb.systems.gun.StationaryGunSystem;

public class AiStaticAttackHelper {

    private static final Vector2 posDiff = new Vector2();

    public static void step(Entity attackerE, int targetE) {
        Box2dBodyComponent attackerBody = Box2dBodySystem.components.get(attackerE);
        Box2dBodyComponent targetBody = Box2dBodySystem.components.get(targetE);
        Vector2 attackerPos = attackerBody.body.getPosition();
        Vector2 targetPos = targetBody.body.getPosition();
        posDiff.set(targetPos).sub(attackerPos);

        float angleRadToTarget = MathUtilsExt.absRadians(posDiff.angleRad());
        float attackerAngle = MathUtilsExt.absRadians(attackerBody.body.getAngle());

        float angleDiff = MathUtilsExt.diffAbsoluteRadians(attackerAngle, angleRadToTarget);
        if (Math.abs(angleDiff) < AiDynamicAttackHelper.TARGET_ALMOST_GET_FROM_ANGLE) {
            GunComponent gunComponent = GunSystem.components.get(attackerE);
            gunComponent.makeShot();
        } else {
            StationaryGunComponent stationaryGunComponent = StationaryGunSystem.components.get(attackerE);
            stationaryGunComponent.setDesiredWorldAngleRadians(angleRadToTarget);
        }
    }
}
