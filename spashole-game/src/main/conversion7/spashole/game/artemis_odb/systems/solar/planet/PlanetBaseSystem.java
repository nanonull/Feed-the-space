package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Align;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.CommonMappers;
import conversion7.spashole.game.artemis_odb.systems.Actor2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.DestroyEntityComponent;
import conversion7.spashole.game.artemis_odb.systems.DestroyEntitySystem;
import conversion7.spashole.game.artemis_odb.systems.HealthComponent;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dCollisionTriggerComp;
import conversion7.spashole.game.stages.SolarSystemScene;
import org.testng.Assert;

import java.util.UUID;

public class PlanetBaseSystem extends IteratingSystem {
    public static ComponentMapper<PlanetBaseComp> components;
    private static final float DELTA = 0.1f;

    public PlanetBaseSystem() {
        super(Aspect.all(PlanetBaseComp.class, HealthComponent.class));
    }

    public static void createBase(Entity planet, Stage stage, int position) {
        // entity
        Entity baseE = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(baseE);

        PlanetComponent planetComponent = PlanetManager.planetComponents.get(planet);
        planetComponent.baseEntity = baseE;
        Position2dSystem.components.create(baseE);
        Rotation2dSystem.components.create(baseE);
        NameManager.components.create(baseE).name =
                "PlanetBase-" + NameManager.getSysName(planet.getId());
        HealthSystem.components.create(baseE).setup(SolarSystemScene.PLANET_BASE_HEALTH);

        PlanetBaseComp planetBaseComp = components.create(baseE);
        planetComponent.baseComponent = planetBaseComp;
        planetBaseComp.planetUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(planet);
        RaceComp raceComp = RaceManager.components.get(planet);
        RaceManager.components.create(baseE);
        planetBaseComp.planetPosition = position;

        Assert.assertNull(planetComponent.planetEntities[position]);
        planetComponent.planetEntities[position] = uuid;

        Box2dCollisionTriggerComp box2dCollisionTriggerComp = CommonMappers.collisionTriggers.create(baseE);
        box2dCollisionTriggerComp.trigger = () -> {
            RaceComp hurtByRace = RaceManager.components.getSafe(box2dCollisionTriggerComp.collideWith);
            if (hurtByRace != null
                    && !RaceManager.areAllies(raceComp.race, hurtByRace.race)) {
                planetBaseComp.hurtByNotAlly = hurtByRace.race;
            }
        };

        // box2d
        Body rigidBody = SpasholeApp.box2dWorld.createBody(Box2dBodySystem.END_INERTIA_BODY_DEF);
        rigidBody.setUserData(uuid);
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(baseE);
        box2dBodyComponent.body = rigidBody;
        Shape shape = new CircleShape();
        shape.setRadius(PlanetManager.BASE_RADIUS_WORLD);
        rigidBody.createFixture(shape, 1);
//        rigidBody.setTransform(worldX, worldY, 0);

        PlanetManager.attachBox2dObject(planet.getId()
                , baseE.getId()
                , PlanetManager.getPositionAngle(planet.getId(), position), new WeldJointDef());

        // visual
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(baseE);
        // world to scene
        Group worldGroup = new Group();
        stage.addActor(worldGroup);
        actor2dComponent.actor = worldGroup;
        worldGroup.setZIndex(0);
        worldGroup.setOrigin(Align.center);
        worldGroup.setScale(SpasholeApp.WORLD_SCALE);

        Group sceneGroup = new Group();
        worldGroup.addActor(sceneGroup);
        sceneGroup.setOrigin(Align.center);
        sceneGroup.setRotation(-90);

        Image building = new Image(SpasholeAssets.house);
        sceneGroup.addActor(building);
        building.setOrigin(Align.center);
        building.setScale(0.31f);
        Actor2dManager.adjustPositionCenterBySize(building);
        building.setY(building.getY() + 12);

        Image flag = new Image(SpasholeAssets.flag);
        sceneGroup.addActor(flag);
        planetBaseComp.flag = flag;
        flag.setOrigin(Align.center);
        flag.setScale(0.75f);
        flag.setRotation(-25);
        flag.setZIndex(0);
        Actor2dManager.adjustPositionCenterBySize(flag);
        flag.setX(flag.getX() + 38);
        flag.setY(flag.getY() + 18);

        // health bar
        ProgressBar progressBar = new ProgressBar(0, 1, 0.05f, false, PlanetManager.PLANET_BASE_BAR_STYLE);
        sceneGroup.addActor(progressBar);
        planetBaseComp.healthBar = progressBar;
        progressBar.setSize(85, 10);
        Actor2dManager.adjustPositionCenterBySize(progressBar);
        progressBar.setY(progressBar.getY() + 44);

    }

    @Override
    protected void process(int entity) {
        PlanetBaseComp planetBaseComp = components.get(entity);
        HealthComponent healthComponent = HealthSystem.components.get(entity);

        DestroyEntityComponent destroyEntityComponent = DestroyEntitySystem.components.getSafe(entity);
        if (destroyEntityComponent != null) {
            // never destroy base
            DestroyEntitySystem.components.remove(entity);
            if (planetBaseComp.hurtByNotAlly == null) {
                // wait for capture
                healthComponent.health = 1;
            } else {
                healthComponent.health = SolarSystemScene.PLANET_BASE_HEALTH;
                Entity planetEntity = SpasholeApp.ARTEMIS_UUIDS.getEntity(planetBaseComp.planetUuid);
                PlanetManager.capture(planetEntity, planetBaseComp.hurtByNotAlly);
            }
        }

        planetBaseComp.healthBar.setValue(healthComponent.getHealthPercent());
        planetBaseComp.hurtByNotAlly = null;
    }
}