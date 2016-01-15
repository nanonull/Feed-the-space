package conversion7.spashole.game.artemis_odb.systems.solar;

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
import conversion7.gdxg.core.custom2d.ui_logger.UiLogger;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.Actor2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorManager;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetManager;
import conversion7.spashole.game.stages.SolarSystemScene;

import java.util.UUID;

public class SunManager extends Manager {
    public static ComponentMapper<SunComponent> components;
    public static final float SUN_DMG_AREA = PlanetManager.PLANET_RADIUS_WORLD * 3f;
    public static UUID sunEntityUuid;
    public static final float SUN_BODY_RADIUS = PlanetManager.PLANET_RADIUS_WORLD;

    public static int create(float worldX, float worldY, Stage stage) {
        // artemis entity
        Entity sunE = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        sunEntityUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(sunE);
        components.create(sunE);
        Position2dSystem.components.create(sunE);
        Rotation2dSystem.components.create(sunE);
        NameManager.components.create(sunE).name = "Sun";

        // box2d
        Body rigidBody = SpasholeApp.box2dWorld.createBody(Box2dBodySystem.END_INERTIA_BODY_DEF);
        rigidBody.setUserData(sunEntityUuid);
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(sunE);
        box2dBodyComponent.body = rigidBody;
        Shape shape = new CircleShape();
        shape.setRadius(SUN_BODY_RADIUS);
        rigidBody.createFixture(shape, 1000);
        rigidBody.setTransform(worldX, worldY, 0);

        Box2dSensorManager.addSensor(sunEntityUuid, worldX, worldY, 0, SUN_DMG_AREA);
        Box2dSensorComponent sensorComponent = Box2dSensorManager.components.get(sunE);
        sensorComponent.startTrigger = entity -> {
            if (HealthSystem.components.has(entity)) {
                if (SolarSystemScene.getPlayerEntity() == entity) {
                    SunHeatDmgSystem.components.create(entity).setup(500, () -> {
                        HealthSystem.components.get(entity).health--;
                        UiLogger.addInfoLabel("Sun heat damage!");
                    });
                } else {
                    SunHeatDmgSystem.components.create(entity).setup(500, () -> {
                        HealthSystem.components.get(entity).health--;
                    });
                }

            }
            return true;
        };
        sensorComponent.endTrigger = integer -> {
            SunHeatDmgSystem.components.remove(integer);
            return true;
        };

        // visual
        Group group = new Group();
        stage.addActor(group);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(sunE);
        actor2dComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeApp.WORLD_SCALE);

        Image image = new Image(SpasholeAssets.sun2);
        group.addActor(image);
        image.setOrigin(Align.center);
        image.setScale(1.19f); // 500x500
        Actor2dManager.adjustPositionCenterBySize(image);

        return sunE.getId();
    }
}