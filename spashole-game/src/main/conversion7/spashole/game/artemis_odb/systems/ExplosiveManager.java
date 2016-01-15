package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import org.slf4j.Logger;

public class ExplosiveManager extends Manager {
    private static final Logger LOG = Utils.getLoggerForClass();
    public static ComponentMapper<ExplosiveComponent> components;

    public static void createExplosion(int forEntityId) {
        Actor2dComponent explodedActor = Actor2dManager.components.getSafe(forEntityId);
        if (explodedActor == null || explodedActor.actor == null) {
            return;
        }
        Position2dComponent explodedPosition = Position2dSystem.components.get(forEntityId);

        // artemis
        int explosionEntity = SpasholeApp.ARTEMIS_ENGINE.create();
        LOG.debug("explosionEntity {}", NameManager.getSysName(explosionEntity));
        Position2dComponent position2dComponent = Position2dSystem.components.create(explosionEntity);
        position2dComponent.pos.x = explodedPosition.pos.x;
        position2dComponent.pos.y = explodedPosition.pos.y;

        // animation
        Animation animation = new Animation(1 / 8f,
                SpasholeAssets.explosion1
                , SpasholeAssets.explosion3
                , SpasholeAssets.explosion6
                , SpasholeAssets.explosion8
                , SpasholeAssets.explosion12
        );
        TextureAnimation2dComponent animation2dComponent =
                TextureAnimation2dSystem.components.create(explosionEntity);
        animation2dComponent.animation = animation;
        animation2dComponent.mode = TextureAnimation2dComponent.Mode.DESTROY_ENTITY_ON_END;

        // image
        Image image = new Image(animation.getKeyFrames()[0]);
        explodedActor.actor.getStage().addActor(image);
        image.setOrigin(Align.center);
        image.setScale(SpasholeApp.WORLD_SCALE);
        Actor2dManager.components.create(explosionEntity).actor = image;
    }
}
