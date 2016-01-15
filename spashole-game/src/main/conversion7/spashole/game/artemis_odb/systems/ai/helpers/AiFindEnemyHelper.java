package conversion7.spashole.game.artemis_odb.systems.ai.helpers;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.BulletManager;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorManager;
import conversion7.spashole.game.artemis_odb.systems.gun.GunComponent;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import org.junit.Assert;
import org.slf4j.Logger;

import java.util.UUID;

public class AiFindEnemyHelper {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static final int ENTITY_NOT_FOUND = -1;
    public static final TestObstacles TEST_OBSTACLES = new TestObstacles();

    public static void createViewSensor(Entity entity, float radius) {
        Assert.assertTrue(Box2dBodySystem.components.has(entity));
        Assert.assertTrue(RaceManager.components.has(entity));
        Assert.assertTrue(GunSystem.components.has(entity));
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.get(entity);
        Vector2 position = box2dBodyComponent.body.getPosition();
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(entity);

        Body sensor = Box2dSensorManager.addSensor(uuid
                , position.x
                , position.y
                , box2dBodyComponent.body.getAngle()
                , radius);
        Box2dSensorManager.connectSensorToBody(box2dBodyComponent.body, sensor, entity);
    }

    public static int step(Entity entity) {
        Box2dSensorComponent sensorComponent;
        RaceComp raceComp;
        GunComponent gunComponent = GunSystem.components.getSafe(entity);
        if (gunComponent == null) {
            // could not attack without gun
            return ENTITY_NOT_FOUND;
        } else {
            sensorComponent = Box2dSensorManager.components.getSafe(entity);
            if (sensorComponent == null) {
                // could not search enemies if I have no view sensor
                return ENTITY_NOT_FOUND;

            } else {
                raceComp = RaceManager.components.getSafe(entity);
                if (raceComp == null) {
                    // could not identify enemies if I don't know my race
                    return ENTITY_NOT_FOUND;
                }
            }
        }

        int foundEnemy = ENTITY_NOT_FOUND;
        for (UUID uuid : sensorComponent.detectedEntities) {
            Entity otherEntity = SpasholeApp.ARTEMIS_UUIDS.getEntity(uuid);
            RaceComp otherRaceComp = RaceManager.components.getSafe(otherEntity);
            if (otherRaceComp == null) {
                // no race > no relations > have no idea if this is enemy
                continue;
            }
            if (!HealthSystem.components.has(otherEntity)) {
                continue;
            }
            if (BulletManager.components.has(otherEntity)) {
                continue;
            }
            if (RaceManager.areEnemies(raceComp.race, otherRaceComp.race)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Entity '{}' has enemy '{}'"
                            , NameManager.getSysName(entity.getId())
                            , NameManager.getSysName(otherEntity.getId()));
                }
                foundEnemy = otherEntity.getId();
                if (raycastAvailable(entity, otherEntity)) {
                    // 1st priority is available enemy
                    break;
                }
            }
        }

        return foundEnemy;
    }

    private static boolean raycastAvailable(Entity entity, Entity otherEntity) {
        TEST_OBSTACLES.setup(otherEntity);
        SpasholeApp.box2dWorld.rayCast(TEST_OBSTACLES,
                Box2dBodySystem.components.get(entity).body.getPosition()
                , Box2dBodySystem.components.get(otherEntity).body.getPosition());

        return !TEST_OBSTACLES.hasObstacles;
    }

    static class TestObstacles implements RayCastCallback {
        public boolean hasObstacles;
        private UUID ignoreFixturesOfEntity;


        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if (fixture.getBody().getUserData() == ignoreFixturesOfEntity) {
                return 1;
            } else {
                hasObstacles = true;
                return 0;
            }
        }

        public void setup(Entity otherEntity) {
            hasObstacles = false;
            ignoreFixturesOfEntity = SpasholeApp.ARTEMIS_UUIDS.getUuid(otherEntity);
        }
    }

}