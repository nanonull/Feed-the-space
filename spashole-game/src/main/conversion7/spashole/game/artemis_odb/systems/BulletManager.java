package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunComponent;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;

import java.util.UUID;

public class BulletManager extends Manager {
    private static final Logger LOG = Utils.getLoggerForClass();
    public static final float BULLET_FORCE_MLT = 500f;
    static BodyDef bulletBodyDef;
    public static ComponentMapper<BulletComponent> components;
    private static int bulletIds;
    private static final float SCALE = SpasholeApp.WORLD_SCALE;
    private static final float RADIUS = SpasholeUtils.sceneToWorld(2.2f);
    private static final float BULL_W = 4;
    private static final float BULL_H = 8;

    static {
        bulletBodyDef = new BodyDef();
        bulletBodyDef.type = BodyDef.BodyType.DynamicBody;
        bulletBodyDef.angularDamping = 3;
        bulletBodyDef.linearDamping = 0f;
    }

    public static void fireBullet(int gunEntityId, int damage, Vector2 startPosition, Vector2 direction) {
        // artemis
        Entity bullet = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(bullet);
        box2dBodyComponent.addAngleDegrees = 90;
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(bullet);
        Position2dSystem.components.create(bullet);
        Rotation2dSystem.components.create(bullet);
        NameManager.components.create(bullet).name = "bullet" + ++bulletIds;
        BulletComponent bulletComponent = components.create(bullet);
        bulletComponent.damage = damage;
        HealthSystem.components.create(bullet);
        SchedulingSystem.schedule(5000, () -> {
        }, bullet.getId());

        RaceComp gunRaceComp = RaceManager.components.getSafe(gunEntityId);
        if (gunRaceComp != null) {
            RaceManager.components.create(bullet).race = gunRaceComp.race;
        }

        // box2d body
        Body body = SpasholeApp.box2dWorld.createBody(bulletBodyDef);
        Shape shape = new CircleShape();
        shape.setRadius(RADIUS);
        body.createFixture(shape, 0.1f);
        shape.dispose();
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(bullet);
        body.setUserData(uuid);
        box2dBodyComponent.body = body;
        body.setBullet(true);

        body.setTransform(startPosition, MathUtilsExt.toRadians(direction.angle()));
        body.applyForceToCenter(
                direction.x * BULLET_FORCE_MLT
                , direction.y * BULLET_FORCE_MLT
                , true);

        // libgdx
        Stage stage = Actor2dManager.components.get(gunEntityId).actor.getStage();

        Group group = new Group();
        stage.addActor(group);
        actor2dComponent.actor = group;
        group.setScale(SCALE);
        group.setOrigin(Align.center);

        Image image = new Image(CommonAssets.pixelWhite);
        group.addActor(image);
        image.setOrigin(Align.center);
        image.setSize(BULL_W, BULL_H);
        Actor2dManager.adjustPositionCenterBySize(image);

        GunComponent gunComponent = GunSystem.components.get(gunEntityId);
        image.setColor(gunComponent.bulletColor);

        if (LOG.isDebugEnabled()) {
            LOG.debug("created bullet {}", NameManager.getSysName(bullet.getId()));
        }
    }

}