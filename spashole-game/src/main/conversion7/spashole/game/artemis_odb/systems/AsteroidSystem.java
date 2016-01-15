package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.artemis_odb.systems.gun.StationaryGunSystem;
import conversion7.spashole.game.artemis_odb.systems.time.IntervalExecutorSystem;
import conversion7.spashole.game.stages.SolarSystemScene;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AsteroidSystem extends IteratingSystem {

    private static final Logger LOG = Utils.getLoggerForClass();
    public static final int MAX_ASTEROID_LEVEL = 3;
    private static final Map<Integer, Float> ACTOR_SCALE_LEVELS = new HashMap<>();
    private static final Map<Integer, Float> BOX2D_RADIUS_LEVELS = new HashMap<>();
    public static ComponentMapper<AsteroidComponent> components;
    static int asteroidIds;
    private static final float GUN_OFFSET_POSITION = 4f;

    static final BodyDef ASTEROID_BODY_DEFINITION = Box2dBodySystem.INF_INERTIA_BODY_DEF;
    public static final float GUN_BARREL_DST = SpasholeUtils.sceneToWorld(20);
    private Vector2 positionOffsetWip = new Vector2();
    private Vector2 newPositionWip = new Vector2();

    static {
        ACTOR_SCALE_LEVELS.put(MAX_ASTEROID_LEVEL, 0.5f);
        ACTOR_SCALE_LEVELS.put(2, 0.167f);
        ACTOR_SCALE_LEVELS.put(1, 0.06f);

        BOX2D_RADIUS_LEVELS.put(MAX_ASTEROID_LEVEL, 80f);
        BOX2D_RADIUS_LEVELS.put(2, 26.667f);
        BOX2D_RADIUS_LEVELS.put(1, 8.889f);
    }

    public AsteroidSystem() {
        super(Aspect.all(AsteroidComponent.class));
    }

    public static float getActorScale(AsteroidComponent asteroidComponent) {
        return ACTOR_SCALE_LEVELS.get(asteroidComponent.level);
    }

    public static float getBox2dBodyRadius(AsteroidComponent asteroidComponent) {
        return BOX2D_RADIUS_LEVELS.get(asteroidComponent.level);
    }

    public static Entity createAsteroid(float worldX, float worldY, int level, Stage stage) {
        LOG.debug("createAsteroid: {}:{} level:{}", worldX, worldY, level);
        // artemis
        Entity asteroid = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(asteroid);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(asteroid);
        Position2dSystem.components.create(asteroid);
        Rotation2dSystem.components.create(asteroid);
        NameManager.components.create(asteroid).name = "asteroid" + ++asteroidIds;
        AsteroidComponent asteroidComponent = components.create(asteroid);
        asteroidComponent.level = level;
        HealthSystem.components.create(asteroid).setup(SolarSystemScene.ASTERIOD_HP_PER_LVL * level);

        // libgdx
        Image image = new Image(SpasholeAssets.meteor);
        stage.addActor(image);
        image.setOrigin(Align.center);
        image.setScale(SpasholeUtils.sceneToWorld(getActorScale(asteroidComponent)));
        actor2dComponent.actor = image;

        // box2d body
        Body body = SpasholeApp.box2dWorld.createBody(ASTEROID_BODY_DEFINITION);
        Shape shape = new CircleShape();
        shape.setRadius(SpasholeUtils.sceneToWorld(getBox2dBodyRadius(asteroidComponent)));
        body.createFixture(shape, 10);
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(asteroid);
        body.setUserData(uuid);
        box2dBodyComponent.body = body;

        body.setTransform(worldX, worldY, MathUtilsExt.randomRadian());

        LOG.debug("created asteroid {}", NameManager.getSysName(asteroid.getId()));
        return asteroid;
    }

    public static void createAsteroidWithGuns(float x, float y, Stage stage) {
        Entity asteroidEntity = createAsteroid(x, y, MAX_ASTEROID_LEVEL, stage);
        Actor2dComponent asteroidActorComponent = Actor2dManager.components.get(asteroidEntity);
        AsteroidComponent asteroidComponent = AsteroidSystem.components.get(asteroidEntity);
        Box2dBodyComponent asteroidBodyComponent = Box2dBodySystem.components.get(asteroidEntity);
        Actor asteroidBodyActor = asteroidActorComponent.actor;

        Group group = new Group();
        stage.addActor(group);
        asteroidActorComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeUtils.sceneToWorld(1));

        // ASTEROID
        group.addActor(asteroidBodyActor);
        Actor2dManager.adjustPositionCenterBySize(asteroidBodyActor);
        asteroidBodyActor.setScale(getActorScale(asteroidComponent)); // reset scale, group will manage it

        Vector2 position1 = asteroidBodyComponent.body.getPosition();
        position1.x += SpasholeUtils.sceneToWorld(BOX2D_RADIUS_LEVELS.get(MAX_ASTEROID_LEVEL) + GUN_OFFSET_POSITION);
        attachGunToAsteroid(stage, asteroidEntity, position1, 0);

        Vector2 position2 = asteroidBodyComponent.body.getPosition();
        position2.x -= SpasholeUtils.sceneToWorld(BOX2D_RADIUS_LEVELS.get(MAX_ASTEROID_LEVEL) + GUN_OFFSET_POSITION);
        attachGunToAsteroid(stage, asteroidEntity, position2, MathUtilsExt.toRadians(180));

        // APPLY ROTATION TO ASTEROID
        asteroidBodyComponent.body.setAngularVelocity(0.5f);
        asteroidBodyComponent.body.setAngularDamping(0);
    }

    private static void attachGunToAsteroid(Stage stage, Entity asteroidEntity, Vector2 positionOnAsteroidRadius,
                                            float radians) {
        Box2dBodyComponent asteroidBodyComponent = Box2dBodySystem.components.get(asteroidEntity);

        Entity gunEntity = StationaryGunSystem.create(3, GUN_BARREL_DST, asteroidBodyComponent, stage);
        IntervalExecutorSystem.schedule(1000, () -> {
            GunSystem.components.get(gunEntity).makeShot();
        }, gunEntity.getId());

        Box2dBodyComponent gunBodyComponent = Box2dBodySystem.components.get(gunEntity);
        gunBodyComponent.body.setTransform(positionOnAsteroidRadius, radians);

        // CONNECT GUN AND ASTEROID
        WeldJointDef jointDef = new WeldJointDef();
        jointDef.initialize(asteroidBodyComponent.body, gunBodyComponent.body, asteroidBodyComponent.body.getPosition());
        SpasholeApp.box2dWorld.createJoint(jointDef);
    }

    public static void createRandomGroupOfAsteroids(float worldX, float worldY, Stage stage) {
        for (int i = 0; i < 11; ) {
            int nextLevel = Utils.RANDOM.nextInt(3) + 1;
            Vector2 positionOnCircle = MathUtilsExt.getPositionOnCircle(
                    Utils.RANDOM.nextInt(MathUtilsExt.DEGREES_MAX)
                    , SpasholeUtils.sceneToWorld(Utils.RANDOM.nextInt(150) + 10));
            positionOnCircle.add(worldX, worldY);
            createAsteroid(positionOnCircle.x
                    , positionOnCircle.y
                    , nextLevel
                    , stage);
            i += nextLevel;
        }
    }

    @Override
    protected void process(int entityId) {
        if (DestroyEntitySystem.components.has(entityId)) {
            AsteroidComponent asteroidComponent = components.get(entityId);
            if (asteroidComponent.level > 1) {
                breakAsteroid(asteroidComponent, entityId);
            }

            ExplosiveManager.createExplosion(entityId);
        }
    }

    private void breakAsteroid(AsteroidComponent initialAsteroid, int initialEntity) {

        Position2dComponent initialPosition = Position2dSystem.components.get(initialEntity);
        Actor2dComponent initialActor = Actor2dManager.components.get(initialEntity);

        // some magic:
        int placeRadius = (int) (initialAsteroid.level * initialAsteroid.level
                * SpasholeApp.graphic.scaleByViewPort(7));
        float moveForce = placeRadius / 2f;

        for (int i = 0; i < Utils.RANDOM.nextInt(2) + 3; i++) {
            // random point on circle
            float angle = (float) (Math.random() * Math.PI * 2);
            MathUtilsExt.getPositionOnCircle(angle, placeRadius, positionOffsetWip);

            newPositionWip.set(
                    initialPosition.pos.x + positionOffsetWip.x
                    , initialPosition.pos.y + positionOffsetWip.y);

            Entity asteroidPiece = createAsteroid(
                    newPositionWip.x, newPositionWip.y
                    , initialAsteroid.level - 1
                    , initialActor.actor.getStage());
            Box2dBodySystem.applyForceToMoveBy(
                    0.5f - Utils.RANDOM.nextFloat() * moveForce,
                    0.5f - Utils.RANDOM.nextFloat() * moveForce,
                    Box2dBodySystem.components.get(asteroidPiece).body);
        }
    }
}