package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;

public class Box2dMotorJointManager extends Manager {
    public static ComponentMapper<Box2dMotorJointComp> components;

    public static void create(int entity, RevoluteJoint joint) {
        components.create(entity).joint = joint;
    }
}
