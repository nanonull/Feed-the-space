package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorManager;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.stages.SolarSystemScene;
import conversion7.spashole.game.utils.SpasholeUtils;

import java.util.UUID;

public class HumanManager extends Manager {
    public static ComponentMapper<HumanComponent> components;

    public static UUID createHumanPerson(float worldX, float worldY, Stage stage) {
        // artemis entity
        Entity person = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(person);
        components.create(person);
        Position2dSystem.components.create(person);
        Rotation2dSystem.components.create(person);
        ExplosiveManager.components.create(person);
        HealthSystem.components.create(person).setup(SolarSystemScene.HUMAN_HP);
        NameManager.components.create(person).name = "person";
        GunSystem.createGun(SolarSystemScene.HUMAN_DMG, SpasholeUtils.sceneToWorld(10), person.getId());
        MovementConstantManager.components.create(person).constant = MovementConstant.HUMAN_PARAMS;

        // box2d body
        Body body = SpasholeApp.box2dWorld.createBody(Box2dBodySystem.END_INERTIA_BODY_DEF);
        body.setUserData(uuid);
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(person);
        box2dBodyComponent.body = body;
        Shape shape = new CircleShape();
        shape.setRadius(SolarSystemScene.PERSON_RADIUS);
        body.createFixture(shape, 1);
        body.setTransform(worldX, worldY, 3f);

        Body activatorSensor = Box2dSensorManager.addSensor(
                uuid
                , worldX
                , worldY
                , box2dBodyComponent.body.getAngle()
                , SolarSystemScene.PERSON_SENSOR_RADIUS);
        Box2dSensorManager.connectSensorToBody(body, activatorSensor, person);

        // visual
        Group group = new Group();
        stage.addActor(group);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(person);
        actor2dComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeApp.WORLD_SCALE);

        Image mainImage = new Image(SpasholeAssets.player_schematic);
        group.addActor(mainImage);
        mainImage.setOrigin(Align.center);
        mainImage.setScale(1);
        Actor2dManager.adjustPositionCenterBySize(mainImage);

        return uuid;
    }
}
