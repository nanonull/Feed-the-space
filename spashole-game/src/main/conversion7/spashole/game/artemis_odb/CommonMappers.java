package conversion7.spashole.game.artemis_odb;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dCollisionTriggerComp;

public class CommonMappers extends Manager {
    public static ComponentMapper<Box2dCollisionTriggerComp> collisionTriggers;
}