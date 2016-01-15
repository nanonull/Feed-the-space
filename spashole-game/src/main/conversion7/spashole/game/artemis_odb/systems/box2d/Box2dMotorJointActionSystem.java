package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.artemis_odb.systems.gun.StationaryGunSystem;
import org.slf4j.Logger;

public class Box2dMotorJointActionSystem extends IteratingSystem {
    public static ComponentMapper<Box2dMotorJointActionComp> components;
    private static final Logger LOG = Utils.getLoggerForClass();

    public Box2dMotorJointActionSystem() {
        super(Aspect.all(Box2dMotorJointActionComp.class, Box2dMotorJointComp.class));
    }

    @Override
    protected void process(int entityId) {
        Box2dMotorJointActionComp motorJointActionComp = components.get(entityId);
        Box2dMotorJointComp motorJointComp = Box2dMotorJointManager.components.get(entityId);
        float jointAngle = motorJointComp.joint.getJointAngle();
        if (jointAngle >= StationaryGunSystem.ROTATION_LIMIT
                && motorJointActionComp.force > 0) {
            motorJointComp.joint.setMotorSpeed(0);
            LOG.debug("Upper bound!");
            return;
        }
        if (jointAngle <= -StationaryGunSystem.ROTATION_LIMIT
                && motorJointActionComp.force < 0) {
            motorJointComp.joint.setMotorSpeed(0);
            LOG.debug("Lower bound!");
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("motorJointActionComp.force {}", motorJointActionComp.force);
            LOG.debug("jointAngle {}", jointAngle);
        }
        motorJointComp.joint.setMotorSpeed(motorJointActionComp.force);
    }
}