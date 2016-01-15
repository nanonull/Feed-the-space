package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.stages.SolarSystemScene;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.junit.Assert;

import java.util.UUID;

public class ShipManager extends Manager {
    public static ComponentMapper<ShipComponent> components;
    public static final float SHIP_RADIUS_WORLD = SpasholeUtils.sceneToWorld(50f);

    public static void setPilot(UUID pilotUuid, Entity shipEntity) {
        ShipComponent shipComponent = ShipManager.components.get(shipEntity);
        Entity pilotEntity = SpasholeApp.ARTEMIS_UUIDS.getEntity(pilotUuid);
        Assert.assertTrue("Human only!", HumanManager.components.has(pilotEntity));
        shipComponent.pilotUuid = pilotUuid;
        DisableEntitySystem.components.create(pilotEntity);

        PlayerInputSystem.setTarget(shipEntity.getId());
    }


    public static Entity createShip(float worldX, float worldY, Stage stage, boolean grok) {
        // artemis entity
        Entity ship = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        UUID shipUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(ship);
        ShipComponent shipComponent = components.create(ship);
        Position2dSystem.components.create(ship);
        Rotation2dSystem.components.create(ship);
        ExplosiveManager.components.create(ship);
        HealthSystem.components.create(ship).setup(DebugFlags.AI_SHIP_NORM_HP
                ? SolarSystemScene.SHIP_HP : 1);
        NameManager.components.create(ship).name = "ship";
        GunSystem.createGun(SolarSystemScene.SHIP_GUN_DMG, SpasholeUtils.sceneToWorld(53), ship.getId());
        MovementConstantManager.components.create(ship).constant = MovementConstant.SHIP_PARAMS;

        // box2d
        Body rigidBody = SpasholeApp.box2dWorld.createBody(Box2dBodySystem.END_INERTIA_BODY_DEF);
        rigidBody.setUserData(shipUuid);
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(ship);
        box2dBodyComponent.body = rigidBody;
        Shape shape = new CircleShape();
        shape.setRadius(SHIP_RADIUS_WORLD);
        rigidBody.createFixture(shape, 10);
        rigidBody.setTransform(worldX, worldY, 3f);

        // visual
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(ship);
        Group shipGroup = new Group();
        stage.addActor(shipGroup);
        actor2dComponent.actor = shipGroup;
        shipGroup.setZIndex(0);
        shipGroup.setOrigin(Align.center);
        shipGroup.setScale(SpasholeApp.WORLD_SCALE);

        TextureRegion textureRegion;
        if (grok) {
            textureRegion = SpasholeAssets.shipRed;
        } else {
            textureRegion = SpasholeAssets.shipBlue;
        }
        Image shipImage = new Image(textureRegion);
        shipGroup.addActor(shipImage);
        shipComponent.shipImg = shipImage;
        shipImage.setOrigin(Align.center);
        shipImage.setScale(0.4f);
        Actor2dManager.adjustPositionCenterBySize(shipImage);

        Image gunImage = new Image(SpasholeAssets.gun);
        shipGroup.addActor(gunImage);
        shipComponent.gunImage = gunImage;
        gunImage.setScale(0.7f);
        gunImage.setOrigin(Align.center);
        gunImage.setRotation(-90);
        Actor2dManager.adjustPositionCenterBySize(gunImage);
        gunImage.setX(gunImage.getX() + 32);
        gunImage.setZIndex(0);

        return ship;

    }

    public static Entity createShip(float worldX, float worldY, Stage stage) {
        return createShip(worldX, worldY, stage, false);
    }
}
