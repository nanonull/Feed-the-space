package conversion7.spashole.game.artemis_odb.systems.gun;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.Actor2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.ExplosiveManager;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dMotorJointActionComp;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dMotorJointActionSystem;
import conversion7.spashole.game.stages.SolarSystemScene;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;

import java.util.UUID;

public class StationaryGunSystem extends IntervalEntityProcessingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static ComponentMapper<StationaryGunComponent> components;
    private static final float DELTA = 0.1f;
    private static final float FORCE_DELTA = DELTA * 10;
    public static final float ROTATION_LIMIT = MathUtilsExt.toRadians(65);
    private static final float GUN_SCALE = SpasholeUtils.sceneToWorld(1);

    public StationaryGunSystem() {
        super(Aspect.all(StationaryGunComponent.class
                , Box2dBodyComponent.class), DELTA);
    }

    public static Entity create(int damage, float barrelDistance
            , Box2dBodyComponent baseBody, Stage stage) {
        LOG.info("create");
        Entity gun = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        GunComponent gunComponent = GunSystem.components.create(gun);
        gunComponent.damage = damage;
        gunComponent.barrelDistance = barrelDistance;
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(gun);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(gun);
        Position2dSystem.components.create(gun);
        Rotation2dSystem.components.create(gun);
        HealthSystem.components.create(gun).setup(SolarSystemScene.STAT_GUN_HP);
        NameManager.components.create(gun).name = "StationaryGun" + ++GunSystem.gunIds;
        ExplosiveManager.components.create(gun);

        Body body = SpasholeApp.box2dWorld.createBody(GunSystem.GUN_BODY_DEFINITION);
        box2dBodyComponent.body = body;
        Shape shape = new CircleShape();
        shape.setRadius(SpasholeUtils.sceneToWorld(11));
        body.createFixture(shape, 1f);
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(gun);
        body.setUserData(uuid);


        Group group = new Group();
        stage.addActor(group);
        actor2dComponent.actor = group;
        group.setScale(GUN_SCALE);
        group.setOrigin(Align.center);
        group.setZIndex(0);

        Image gunImage = new Image(SpasholeAssets.gun);
        group.addActor(gunImage);
        gunImage.setOrigin(Align.center);
        gunImage.setScale(0.7f);
        Actor2dManager.adjustPositionCenterBySize(gunImage);
        gunImage.setRotation(-90f);

        return gun;
    }

    @Override
    protected void process(Entity e) {
        // TODO if baseBody was destroyed then disable rotation
        StationaryGunComponent gunComponent = components.get(e);
        Box2dBodyComponent bodyComponent = Box2dBodySystem.components.get(e);

        float desiredWorldAngle = gunComponent.getDesiredWorldAngleRads();
        float currWorldAngle = MathUtilsExt.absRadians(bodyComponent.body.getAngle());
        float angleDelta = MathUtilsExt.diffAbsoluteRadians(currWorldAngle, desiredWorldAngle);
        if (MathUtilsExt.areDecimalsEqual(angleDelta, 0)) {
            return;
        }

        // act motor
        Box2dMotorJointActionComp motorJointActionComp = Box2dMotorJointActionSystem.components.create(e);
        motorJointActionComp.force = (angleDelta / Math.abs(angleDelta)) * FORCE_DELTA;
    }
}