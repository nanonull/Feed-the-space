package conversion7.spashole.game.artemis_odb.systems.ai;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.badlogic.gdx.math.Vector2;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiDynamicMoveHelper;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiDynamicRotateOnTargetHelper;

import java.util.UUID;

public class AiDynamicMoveToEntityManager extends Manager {
    public static ComponentMapper<AiMoveToEntityComp> components;
    private static final Vector2 posDiff = new Vector2();

    public static void step(int entityId, int targetE) {
        boolean targetAngleReached = AiDynamicRotateOnTargetHelper
                .step(entityId, targetE, posDiff);

        if (targetAngleReached) {
            AiDynamicMoveHelper.step(entityId);
        }
    }

    public static void create(int entityMoving, UUID targetUuid, float distanceToReach) {
        AiMoveToEntityComp aiMoveToEntityComp = components.create(entityMoving);
        aiMoveToEntityComp.moveTo = targetUuid;
        aiMoveToEntityComp.distanceToReach = distanceToReach;
    }

    public static boolean isNotCompleted(int entityId, AiMoveToEntityComp moveToEntityComp) {
        if (moveToEntityComp.moveTo != null) {
            Position2dComponent entPos = Position2dSystem.components.get(entityId);
            Position2dComponent targPos = Position2dSystem.components.get(moveToEntityComp.getTargetId());
            if (moveToEntityComp.distanceToReach < entPos.pos.dst(targPos.pos)) {
                return true;
            }
        }

        components.remove(entityId);
        return false;
    }
}
