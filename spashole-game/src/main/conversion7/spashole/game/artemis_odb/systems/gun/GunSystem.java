package conversion7.spashole.game.artemis_odb.systems.gun;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.artemis_odb.systems.BulletManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import conversion7.spashole.game.utils.PointEffectHelper;

public class GunSystem extends IteratingSystem {

    public static ComponentMapper<GunComponent> components;
    static final BodyDef GUN_BODY_DEFINITION = new BodyDef();
    static int gunIds;
    private Vector2 bulletStartPos = new Vector2();
    private Vector2 bulletDirection = new Vector2();

    static {
        GUN_BODY_DEFINITION.type = BodyDef.BodyType.DynamicBody;
        GUN_BODY_DEFINITION.angularDamping = 2;
        GUN_BODY_DEFINITION.linearDamping = 0.5f;
    }

    public GunSystem() {
        super(Aspect.all(GunComponent.class));
    }

    public static GunComponent createGun(int damage, float barrelDistance, int entity) {
        GunComponent gunComponent = GunSystem.components.create(entity);
        gunComponent.damage = damage;
        gunComponent.barrelDistance = barrelDistance;
        return gunComponent;
    }

    @Override
    protected void process(int entityId) {
        GunComponent gunComponent = components.get(entityId);
        gunComponent.bulletReady += world.getDelta();
        if (gunComponent.fireWhenBulletReady) {
            if (gunComponent.bulletReady < gunComponent.bulletReloadTime) {
                // bullet not ready yet
                return;
            }

            gunComponent.fireWhenBulletReady = false;
            gunComponent.bulletReady = 0;

            Position2dComponent position2dComponent = Position2dSystem.components.get(entityId);
            Rotation2dComponent rotation2dComponent = Rotation2dSystem.components.get(entityId);
            bulletStartPos.set(position2dComponent.pos.x, position2dComponent.pos.y);
            MathUtilsExt.angleRadiansToVector(
                    rotation2dComponent.angleDegrees * MathUtils.degreesToRadians
                    , bulletDirection);
            bulletStartPos.add(bulletDirection.x * gunComponent.barrelDistance
                    , bulletDirection.y * gunComponent.barrelDistance);

            PointEffectHelper.createPoint(bulletStartPos, Color.YELLOW);
            BulletManager.fireBullet(
                    entityId
                    , gunComponent.damage
                    , bulletStartPos
                    , bulletDirection
            );
        }
    }
}
