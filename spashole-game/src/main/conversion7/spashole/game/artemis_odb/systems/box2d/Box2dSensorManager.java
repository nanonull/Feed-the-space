package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import conversion7.spashole.game.SpasholeApp;

import java.util.UUID;

public class Box2dSensorManager extends Manager {
    public static ComponentMapper<Box2dSensorComponent> components;
    public static final BodyDef SENSOR_BODY_DEF = new BodyDef();

    public static Body addSensor(UUID uuidE, float x, float y, float angle, float sensorRadius) {
        Body sensor = SpasholeApp.box2dWorld.createBody(SENSOR_BODY_DEF);
        sensor.setUserData(uuidE);
        Shape shape = new CircleShape();
        shape.setRadius(sensorRadius);
        Fixture fixture = sensor.createFixture(shape, Box2dWorldSystem.SMALL_DENSITY);
        fixture.setSensor(true);
        sensor.setTransform(x, y, angle);
        components.create(SpasholeApp.ARTEMIS_UUIDS.getEntity(uuidE));

        return sensor;
    }

    public static void connectSensorToBody(Body rigidBody, Body activatorSensor, Entity bodyParentEntity) {
        WeldJointDef jointDef = new WeldJointDef();
        jointDef.initialize(rigidBody, activatorSensor, rigidBody.getPosition());
        SpasholeApp.box2dWorld.createJoint(jointDef);
        Box2dAdditionalBodiesComponent additionalBodiesComponent =
                Box2dAdditionalBodiesManager.components.create(bodyParentEntity);
        additionalBodiesComponent.bodies.add(activatorSensor);
    }
}
