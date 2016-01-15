package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;

public class Position2dSystem extends IteratingSystem {

    public static ComponentMapper<Position2dComponent> components;

    public Position2dSystem() {
        super(Aspect.all(Position2dComponent.class, Actor2dComponent.class));
    }

    public static void setPosition(int entityId, float x, float y) {
        if (Box2dBodySystem.components.has(entityId)) {
            Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.get(entityId);
            box2dBodyComponent.body.setTransform(x, y, box2dBodyComponent.body.getAngle());

        } else if (Position2dSystem.components.has(entityId)) {
            Position2dComponent position2dComponent = Position2dSystem.components.get(entityId);
            position2dComponent.pos.x = x;
            position2dComponent.pos.y = y;
        } else {
            Utils.logErrorWithCurrentStacktrace("Could not set position on " + NameManager.getSysName(entityId));
        }
    }

    @Override
    protected void process(int entityId) {
        Position2dComponent position2dComponent = components.get(entityId);
        Actor2dComponent actor2dComponent = Actor2dManager.components.get(entityId);
        float hw = actor2dComponent.actor.getWidth() / 2;
        float hh = actor2dComponent.actor.getHeight() / 2;
        actor2dComponent.actor.setPosition(position2dComponent.pos.x - hw,
                position2dComponent.pos.y - hh);
    }
}
