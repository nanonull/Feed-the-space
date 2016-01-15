package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import conversion7.gdxg.core.CommonApp;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.Aspects;
import conversion7.spashole.game.artemis_odb.CommonMappers;
import conversion7.spashole.game.artemis_odb.systems.BulletComponent;
import conversion7.spashole.game.artemis_odb.systems.BulletManager;
import conversion7.spashole.game.artemis_odb.systems.DestroyEntitySystem;
import conversion7.spashole.game.artemis_odb.systems.EntityInventoryComp;
import conversion7.spashole.game.artemis_odb.systems.EntityInventoryManager;
import conversion7.spashole.game.artemis_odb.systems.HealthComponent;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.InventoryItemComp;
import conversion7.spashole.game.artemis_odb.systems.InventoryItemManager;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.utils.PointEffectHelper;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * Game world connected with Physics world by UUID of entity.<br>
 * No UUID in body's user data - no game logic for entity!
 */
public class Box2dWorldSystem extends BaseSystem {

    private static final Logger LOG = Utils.getLoggerForClass();
    private static final float FIXED_SINGLE_TIME_STEP = 1 / 60f;
    /** max frame time to avoid spiral of death (on slow devices) */
    public static final float FRAME_TIME_LIMIT = 0.25f;
    public static final float SMALL_VELOCITY = 0.0000000001f;
    public static final float SMALL_DENSITY = 0.000001f;
    public static final float BIG_VELOCITY = 100000000;

    private float accumulator = 0;

    /** Warning: make sure your game systems will not request any destroyed body after this! */
    public static void destroyAllBodies() {
        LOG.info("destroyAllBodies");
        IntBag entities = Aspects.BOX2D_BODIES.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            int entId = entities.get(i);
            SpasholeApp.box2dWorld.destroyBody(Box2dBodySystem.components.get(entId).body);
            Box2dAdditionalBodiesComponent additionalBodiesComponent = Box2dAdditionalBodiesManager.components.getSafe(entId);


            if (additionalBodiesComponent != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Destroy add.bodies: {}", NameManager.getSysName(entId));
                }

                for (Body body : additionalBodiesComponent.bodies) {
                    SpasholeApp.box2dWorld.destroyBody(body);
                }
            }
        }
    }

    @Override
    protected void initialize() {
        LOG.info("Box2dWorldSystem initialize");
        Box2D.init();
        SpasholeApp.box2dWorld = new World(new Vector2(0, 0), true);
        SpasholeApp.box2dWorld.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                UUID uuidA = (UUID) fixtureA.getBody().getUserData();
                if (uuidA == null) return;
                Entity entityA = CommonApp.ARTEMIS_UUIDS.getEntity(uuidA);
                if (entityA == null) return;

                Fixture fixtureB = contact.getFixtureB();
                UUID uuidB = (UUID) fixtureB.getBody().getUserData();
                if (uuidB == null) return;
                Entity entityB = CommonApp.ARTEMIS_UUIDS.getEntity(uuidB);
                if (entityB == null) return;

                if (isSensorContactWithBody(fixtureA, fixtureB)) {
                    startSensorDetectsBody(entityA, uuidB, entityB);
                }
                if (isSensorContactWithBody(fixtureB, fixtureA)) {
                    startSensorDetectsBody(entityB, uuidA, entityA);
                }

                if (!fixtureA.isSensor() && !fixtureB.isSensor()) {
                    WorldManifold worldManifold = contact.getWorldManifold();
                    PointEffectHelper.createPoint(worldManifold.getPoints()[0], Color.WHITE);
                    computeRigidCollision(entityA, entityB);
                    computeRigidCollision(entityB, entityA);
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                UUID uuidA = (UUID) fixtureA.getBody().getUserData();
                if (uuidA == null) return;
                Entity entityA = CommonApp.ARTEMIS_UUIDS.getEntity(uuidA);
                if (entityA == null) return;

                Fixture fixtureB = contact.getFixtureB();
                UUID uuidB = (UUID) fixtureB.getBody().getUserData();
                if (uuidB == null) return;
                Entity entityB = CommonApp.ARTEMIS_UUIDS.getEntity(uuidB);
                if (entityB == null) return;

                if (isSensorContactWithBody(fixtureA, fixtureB)) {
                    endSensorDetectsBody(entityA, uuidB, entityB);
                }
                if (isSensorContactWithBody(fixtureB, fixtureA)) {
                    endSensorDetectsBody(entityB, uuidA, entityA);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        SpasholeApp.box2dWorldDebugRenderer = new Box2DDebugRenderer();
    }

    private boolean isSensorContactWithBody(Fixture fixtureA, Fixture fixtureB) {
        return fixtureA.isSensor() && !fixtureB.isSensor();
    }

    private void startSensorDetectsBody(Entity entityWithSensor, UUID entityUuidDetectedBySensor
            , Entity entityDetectedBySensor) {
        Box2dSensorComponent sensorComponent = Box2dSensorManager.components.getSafe(entityWithSensor);
        if (sensorComponent == null) {
            return;
        }

        sensorComponent.detectedEntities.add(entityUuidDetectedBySensor);
        if (sensorComponent.startTrigger != null) {
            sensorComponent.startTrigger.test(entityDetectedBySensor.getId());
        }
    }

    private void endSensorDetectsBody(Entity entityWithSensor, UUID entityUuidComesOutFromSensorContact
            , Entity entityComesOutFromSensorContact) {
        Box2dSensorComponent sensorComponent = Box2dSensorManager.components.getSafe(entityWithSensor);
        if (sensorComponent == null) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("endSensorDetectsBody");
            LOG.debug("EntityWithSensor: {}", NameManager.getSysName(entityWithSensor.getId()));
            LOG.debug("entityUuidComesOutFromSensorContact: {}", NameManager.getSysName(SpasholeApp.ARTEMIS_UUIDS.getEntity(entityUuidComesOutFromSensorContact).getId()));
        }

        sensorComponent.detectedEntities.remove(entityUuidComesOutFromSensorContact);
        if (sensorComponent.endTrigger != null) {
            sensorComponent.endTrigger.test(entityComesOutFromSensorContact.getId());
        }
    }

    private void computeRigidCollision(Entity entity, Entity collideWithEntity) {
        if (HealthSystem.components.has(entity)) {
            HealthComponent healthComponent = HealthSystem.components.get(entity);
            BulletComponent otherBullet = BulletManager.components.getSafe(collideWithEntity);
            if (otherBullet != null) {
                healthComponent.health -= otherBullet.damage;
                // TODO review: bullet vs inv.items vs other entities
                // doubled hp logic (fix 'bullets are no destroyed on collision with inv-items')
                HealthSystem.components.get(collideWithEntity).health = 0;
            } else {
                if (!InventoryItemManager.components.has(collideWithEntity)) {
                    healthComponent.health--;
                }
            }

            Box2dCollisionTriggerComp collisionTriggerComp = CommonMappers.collisionTriggers.getSafe(entity);
            if (collisionTriggerComp != null) {
                if (collisionTriggerComp.trigger == null) {
                    LOG.error("No collisionTriggerComp.trigger for {}", NameManager.getSysName(entity.getId()));
                } else {
                    collisionTriggerComp.collideWith = collideWithEntity.getId();
                    collisionTriggerComp.trigger.run();
                }
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(NameManager.getSysName(entity.getId()) + " health " + healthComponent.health);
            }
        }

        InventoryItemComp inventoryItemComp = InventoryItemManager.components.getSafe(collideWithEntity);
        if (inventoryItemComp != null
                && !DestroyEntitySystem.components.has(collideWithEntity)) {
            EntityInventoryComp entityInventoryComp = EntityInventoryManager.components.getSafe(entity);
            if (entityInventoryComp != null) {
                entityInventoryComp.addItem(inventoryItemComp.type);
                DestroyEntitySystem.components.create(collideWithEntity);
            }
        }
    }

    @Override
    protected void processSystem() {
        float frameTime = Math.min(SpasholeApp.core.getDelta(), FRAME_TIME_LIMIT);
        accumulator += frameTime;
        while (accumulator >= FIXED_SINGLE_TIME_STEP) {
            SpasholeApp.box2dWorld.step(FIXED_SINGLE_TIME_STEP, 6, 2);
            accumulator -= FIXED_SINGLE_TIME_STEP;
        }
    }
}
