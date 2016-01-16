package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import conversion7.gdxg.core.custom2d.TextureRegionColoredDrawable;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.ResKey;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.Actor2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.AsteroidSystem;
import conversion7.spashole.game.artemis_odb.systems.DestroyEntitySystem;
import conversion7.spashole.game.artemis_odb.systems.EntityEntersCameraViewTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.EntityExitsCameraViewTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.PlayerInfoUpdateSystem;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.AiStaticSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiFindEnemyHelper;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dMotorJointManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dWorldSystem;
import conversion7.spashole.game.artemis_odb.systems.gun.StationaryGunComponent;
import conversion7.spashole.game.artemis_odb.systems.gun.StationaryGunSystem;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;
import org.testng.Assert;

import java.util.UUID;

public class PlanetManager extends Manager {
    private static final Logger LOG = Utils.getLoggerForClass();
    public static ComponentMapper<PlanetComponent> planetComponents;
    public static ComponentMapper<PlanetSpeakerComponent> planetSpeakerComponents;
    public static final ResKey SPIKER_PLANETY = new ResKey("SPIKER_PLANETY");
    public static final float PLANET_RADIUS_WORLD = SpasholeUtils.sceneToWorld(300);
    public static final float BASE_RADIUS_WORLD = SpasholeUtils.sceneToWorld(30);
    private static final float ANGLE_BTW_PLANET_BODIES = (DebugFlags.NORM_ANGLE_BTW_PLANET_BODIES
            ? MathUtilsExt.toRadians(30)
            : MathUtilsExt.toRadians(15));
    public static final int PLANET_POSITIONS = (int) (MathUtilsExt.RADIANS_MAX / ANGLE_BTW_PLANET_BODIES);
    public static ProgressBar.ProgressBarStyle PLANET_BASE_BAR_STYLE;

    static {
        TextureRegionDrawable knobDrawable = new TextureRegionColoredDrawable(Color.YELLOW, SpasholeAssets.pixelWhite);
        TextureRegionDrawable backDrawable = new TextureRegionColoredDrawable(Color.LIGHT_GRAY, SpasholeAssets.pixelWhite);
        PLANET_BASE_BAR_STYLE = new ProgressBar.ProgressBarStyle(backDrawable, knobDrawable);
        PLANET_BASE_BAR_STYLE.knob.setMinHeight(8);
        PLANET_BASE_BAR_STYLE.knobBefore = PLANET_BASE_BAR_STYLE.knob;
        PLANET_BASE_BAR_STYLE.background.setMinHeight(5);
    }

    private static int getRandomPlanetBodyPosition() {
        return SpasholeUtils.RANDOM.nextInt(PLANET_POSITIONS);
    }

    public static PlanetSpeakerComponent getPlanetSpeaker(int id) {
        PlanetSpeakerComponent planetSpeakerComponent = planetSpeakerComponents.create(id);
        if (planetSpeakerComponent.name == null) {
            planetSpeakerComponent.name = SpasholeAssets.textResources.get(SPIKER_PLANETY);
        }
        return planetSpeakerComponent;
    }

    public static Entity create(String name, float worldX, float worldY, Stage stage
            , TextureRegion textureRegion, RaceComp.Race race) {
        LOG.info("Create planet: {}, race: {}", name, race);
        Assert.assertNotNull(name, "Planet name!");

        // entity
        Entity planet = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(planet);
        PlanetComponent planetComponent = planetComponents.create(planet);
        Position2dComponent planetPosition2dComponent = Position2dSystem.components.create(planet);
        Rotation2dSystem.components.create(planet);
        NameManager.components.create(planet).name = name;
        RaceManager.components.create(planet);

        PlanetShipSpawnComp shipSpawnerComp = PlanetShipSpawnSystem.components.create(planet);
        shipSpawnerComp.setup(2000, () -> {
            PlanetShipSpawnSystem.step(planet);
        });

        PlanetAnimalSpawnComp animalSpawnComp = PlanetAnimalSpawnSystem.components.create(planet);
        animalSpawnComp.setup(2000, () -> {
            PlanetAnimalSpawnSystem.step(planet);
        });

        EntityEntersCameraViewTriggerManager.componentCreate(planet.getId()).addRunnable(false, "PlanetInView"
                , () -> {
            PlayerInfoUpdateSystem.visiblePlanets.add(uuid);
        });

        EntityExitsCameraViewTriggerManager.componentCreate(planet.getId()).addRunnable(false, "PlanetOutView"
                , () -> {
            PlayerInfoUpdateSystem.visiblePlanets.remove(uuid);
        });

        // box2d
        Body rigidBody = SpasholeApp.box2dWorld.createBody(Box2dBodySystem.END_INERTIA_BODY_DEF);
        rigidBody.setUserData(uuid);
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(planet);
        box2dBodyComponent.body = rigidBody;
        Shape shape = new CircleShape();
        shape.setRadius(PLANET_RADIUS_WORLD);
        rigidBody.createFixture(shape, 1000);
        rigidBody.setTransform(worldX, worldY, 0);

        // visual
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(planet);
        Group group = new Group();
        stage.addActor(group);
        actor2dComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeApp.WORLD_SCALE);

        Image image = new Image(textureRegion);
        group.addActor(image);
        image.setOrigin(Align.center);
        image.setScale(1.19f); // 500x500
        Actor2dManager.adjustPositionCenterBySize(image);

        PlanetInfoPanel planetInfoPanel = new PlanetInfoPanel(planet.getId());
        group.addActor(planetInfoPanel);
        planetComponent.infoPanel = planetInfoPanel;
        Actor2dManager.adjustPositionCenterBySize(planetInfoPanel);

        PlanetBaseSystem.createBase(planet, stage
                , (DebugFlags.PLANET_BASE_RND_POS ? getRandomPlanetBodyPosition() : 0));
        PlanetManager.capture(planet, race);

        return planet;
    }

    protected static float getPositionAngle(int planetId, int position) {
        Box2dBodyComponent bodyComponent = Box2dBodySystem.components.get(planetId);
        float planetAngle = bodyComponent.body.getAngle();
        return position * ANGLE_BTW_PLANET_BODIES + planetAngle;
    }

    public static Joint attachBox2dObject(
            int planet
            , int attachE
            , float onPlanetAngleRad
            , JointDef jointDef) {
        Box2dBodyComponent planetBody = Box2dBodySystem.components.get(planet);
        Vector2 planetPosition = planetBody.body.getPosition();

        // transform
        Vector2 onCirclePos = MathUtilsExt.getPositionOnCircle(onPlanetAngleRad, PLANET_RADIUS_WORLD);
        onCirclePos.add(planetPosition);
        Box2dBodyComponent objBody = Box2dBodySystem.components.get(attachE);
        objBody.body.setTransform(onCirclePos, onPlanetAngleRad);

        if (jointDef instanceof WeldJointDef) {
            ((WeldJointDef) jointDef).initialize(planetBody.body, objBody.body, onCirclePos);
        } else if (jointDef instanceof RevoluteJointDef) {
            ((RevoluteJointDef) jointDef).initialize(planetBody.body, objBody.body, onCirclePos);
        } else {
            throw new GdxRuntimeException("Not supported joint: " + jointDef.getClass());
        }

        return SpasholeApp.box2dWorld.createJoint(jointDef);
    }

    public static Entity addStaticGun(int planetE, Stage stage, int pos) {
        Box2dBodyComponent planetBody = Box2dBodySystem.components.get(planetE);

        Entity statGun = StationaryGunSystem.create(
                3
                , AsteroidSystem.GUN_BARREL_DST
                , planetBody
                , stage);
        RaceManager.components.create(statGun).race = RaceManager.components.get(planetE).race;
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(statGun);
        planetComponents.get(planetE).planetEntities[pos] = uuid;

        float angle = getPositionAngle(planetE, pos);
        if (LOG.isDebugEnabled()) {
            LOG.debug("addStaticGun pos={} deg={}", pos, MathUtilsExt.toDegrees(angle));
        }

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
//            revoluteJointDef.collideConnected = false; // TODO test turret collision bug (broken angle)
        revoluteJointDef.enableMotor = true;
        revoluteJointDef.maxMotorTorque = Box2dWorldSystem.BIG_VELOCITY;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = -StationaryGunSystem.ROTATION_LIMIT;
        revoluteJointDef.upperAngle = StationaryGunSystem.ROTATION_LIMIT;
        Joint joint = PlanetManager.attachBox2dObject(planetE
                , statGun.getId()
                , angle
                , revoluteJointDef);
        Box2dMotorJointManager.create(statGun.getId(), ((RevoluteJoint) joint));

        // StationaryGunComponent should be created after body transform was set
        Box2dBodyComponent gunBody = Box2dBodySystem.components.get(statGun);
        StationaryGunComponent stationaryGunComponent = StationaryGunSystem.components.create(statGun);
        stationaryGunComponent.setup(gunBody.body.getAngle());

        AiFindEnemyHelper.createViewSensor(statGun, SpasholeUtils.sceneToWorld(550f));
        AiStaticSystem.components.create(statGun);
        return statGun;
    }

    public static void createStaticGuns(int planetId, Stage stage) {
        if (DebugFlags.DONT_CREATE_STATIC_GUNS_ON_START) {
            LOG.info("DebugFlags.DONT_CREATE_STATIC_GUNS_ON_START ON");
            return;
        }

        PlanetComponent planetComponent = planetComponents.get(planetId);
        for (int pos = 0; pos < PLANET_POSITIONS; pos++) {
            UUID entityU = planetComponent.planetEntities[pos];
            if (entityU == null) {
                addStaticGun(planetId, stage, pos);
            }
        }
    }

    /** Race is set only for components with RaceComp already created */
    public static void capture(Entity planetEntity, RaceComp.Race race) {
        if (race == RaceComp.Race.HUMAN) {
            // player for Lumen
            race = RaceComp.Race.LUMEN;
        }
        // planet race
        RaceManager.components.get(planetEntity).race = race;
        // planet items
        PlanetComponent planetComponent = PlanetManager.planetComponents.get(planetEntity);
        for (int i = 0; i < planetComponent.planetEntities.length; i++) {
            UUID entityU = planetComponent.planetEntities[i];
            if (entityU == null) continue;
            Entity entity = SpasholeApp.ARTEMIS_UUIDS.getEntity(entityU);
            if (entity == null
                    || DestroyEntitySystem.components.has(entity)) {
                // TODO validate planetEntities on entity destroy
                planetComponent.planetEntities[i] = null;
            } else {
                RaceComp raceComp = RaceManager.components.getSafe(entity);
                if (raceComp != null) {
                    raceComp.race = race;
                }
            }
        }

        PlanetBaseComp planetBaseComp = PlanetBaseSystem.components.get(planetComponent.baseEntity);
        planetBaseComp.flag.setColor(race.getColor());

        LOG.info("Planet {} was captured by {}"
                , NameManager.getSysName(planetEntity.getId())
                , race);
    }
}