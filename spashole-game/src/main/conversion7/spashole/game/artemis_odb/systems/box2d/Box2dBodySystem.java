package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import org.slf4j.Logger;

public class Box2dBodySystem extends IteratingSystem {

    private static final Logger LOG = Utils.getLoggerForClass();

    public static ComponentMapper<Box2dBodyComponent> components;
    static Vector2 forceWip = new Vector2();
    private static final float MAGIC_MASS_MLT_FOR_ZERO_DAMPING = 30.46f;
    public static final short ACTIVE_BODY = 0x0001;
    public static final short ALL_MASK = -1;
    public static final short INACTIVE_BODY = 0x0008;
    public static final short NOBODY_MASK = 0x0010;
    public static final BodyDef END_INERTIA_BODY_DEF = new BodyDef();
    public static final BodyDef INF_INERTIA_BODY_DEF = new BodyDef();

    static {
        END_INERTIA_BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        END_INERTIA_BODY_DEF.angularDamping = 3;
        END_INERTIA_BODY_DEF.linearDamping = 0.5f;

        INF_INERTIA_BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        INF_INERTIA_BODY_DEF.angularDamping = 0;
        INF_INERTIA_BODY_DEF.linearDamping = 0;

        Box2dSensorManager.SENSOR_BODY_DEF.type = BodyDef.BodyType.DynamicBody;
    }

    public Box2dBodySystem() {
        super(Aspect.all(Box2dBodyComponent.class));
    }


    public static void applyTorqueToRotateBy(float angleRadian, Body body) {
        float mass = body.getMass();
        mass *= 2.5f;
        float force = angleRadian * mass * mass;
        body.applyTorque(force, true);
    }

    /**
     * TODO: distance after forcing body with 0.5f damping is greater on 8% that expected distance.
     * Works as expected for bodies with zero damping.
     */
    public static void applyForceToMoveBy(float byX, float byY, Body body) {
        forceWip.set(byX, byY);
        forceWip.sub(body.getLinearVelocity());
        float mass = body.getMass();
        float linDampingCoef = 1 + body.getLinearDamping();
        linDampingCoef *= linDampingCoef * linDampingCoef;
        forceWip.scl(mass * MAGIC_MASS_MLT_FOR_ZERO_DAMPING * linDampingCoef);
        body.applyForceToCenter(forceWip, true);
    }

    public static void move(
            Box2dBodyComponent bodyComponent
            , Vector2 moveDirection
            , float moveMlt
            , float delta
            , boolean forward) {
        moveMlt *= delta;
        float fx = moveDirection.x * moveMlt;
        float fy = moveDirection.y * moveMlt;
        if (!forward) {
            fx *= -1;
            fy *= -1;
        }
        bodyComponent.body.applyForceToCenter(fx, fy, true);
    }

    /** Use negative torqueForce to rotate in clockwise direction */
    public static void rotate(Box2dBodyComponent bodyComponent, float torqueForce, float delta) {
        bodyComponent.body.applyTorque(torqueForce * delta, true);
    }

    @Override
    protected void process(int entityId) {
        Box2dBodyComponent box2dBodyComponent = components.get(entityId);
        if (Position2dSystem.components.has(entityId)) {
            Position2dComponent position2dComponent = Position2dSystem.components.get(entityId);
            Vector2 position = box2dBodyComponent.body.getPosition();
            position2dComponent.pos.x = position.x;
            position2dComponent.pos.y = position.y;
        }
        if (Rotation2dSystem.components.has(entityId)) {
            Rotation2dComponent rotation2dComponent = Rotation2dSystem.components.get(entityId);
            rotation2dComponent.angleDegrees = MathUtilsExt.toDegrees(box2dBodyComponent.body.getAngle())
                    + box2dBodyComponent.addAngleDegrees;
        }
    }
}
