package conversion7.spashole.game.artemis_odb.systems.solar;

import aurelienribon.tweenengine.Tween;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.tween.ActorAccessor;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.Actor2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;

public class BlackHoleGravitySystem extends IteratingSystem {
    public static ComponentMapper<BlackHoleGravityComp> components;
    private static Entity blackHoleE;
    private static Position2dComponent blackHolePos;
    private static final float GRAVITY_SCL = 0.42f;
    static float onHoleTimeSec = 0.42f;
    private static int onHoleTimeMs = (int) (onHoleTimeSec * 1000);
    private static final float ON_HOLE_DST = SunManager.SUN_BODY_RADIUS * 0.65f;

    public BlackHoleGravitySystem() {
        super(Aspect.all(BlackHoleGravityComp.class
                , Actor2dComponent.class
                , Position2dComponent.class));
    }

    public static void setBlackHole(Entity blackHoleE) {
        BlackHoleGravitySystem.blackHoleE = blackHoleE;
        blackHolePos = Position2dSystem.components.get(blackHoleE);
    }

    @Override
    protected void process(int entityId) {
        BlackHoleGravityComp blackHoleComp = components.get(entityId);
        Actor2dComponent actor2dComponent = Actor2dManager.components.get(entityId);
        Position2dComponent entPos = Position2dSystem.components.get(entityId);

        Vector2 posDiff = new Vector2(entPos.pos).sub(blackHolePos.pos);
        float dstToHole = posDiff.len();
        if (!blackHoleComp.onHole && dstToHole <= ON_HOLE_DST) {
            blackHoleComp.onHole = true;
            Tween.to(actor2dComponent.actor, ActorAccessor.SCALE_XY, onHoleTimeSec)
                    .target(0, 0)
                    .start(SpasholeApp.tweenManager);
            SchedulingSystem.schedule(onHoleTimeMs, () -> {
            }, entityId).killEntity = true;
        }
        posDiff.nor().scl(GRAVITY_SCL);
        entPos.pos.sub(posDiff.x, posDiff.y);
    }
}