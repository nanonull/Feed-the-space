package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import conversion7.gdxg.core.CommonApp;
import conversion7.gdxg.core.utils.Utils;
import org.slf4j.Logger;

public class TextureAnimation2dSystem extends IteratingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static ComponentMapper<TextureAnimation2dComponent> components;

    public TextureAnimation2dSystem() {
        super(Aspect.all(TextureAnimation2dComponent.class));
    }

    @Override
    protected void process(int entityId) {
        TextureAnimation2dComponent animation2dComponent = components.get(entityId);
        if (TextureAnimation2dComponent.Mode.DESTROY_ENTITY_ON_END == animation2dComponent.mode
                && animation2dComponent.stateTime >= animation2dComponent.animation.getAnimationDuration()) {
            DestroyEntitySystem.components.create(entityId);
            LOG.debug("DESTROY_ENTITY_ON_END {}, animation duration = {}"
                    , entityId
                    , animation2dComponent.animation.getAnimationDuration());
            return;
        }

        if (Actor2dManager.components.has(entityId)) {
            Actor2dComponent actor2dComponent = Actor2dManager.components.get(entityId);
            if (actor2dComponent.actor instanceof Image) {
                Image image = (Image) actor2dComponent.actor;
                if (!(image.getDrawable() instanceof TextureRegionDrawable)) {
                    LOG.error("Could not animate actor on entity {}\n" +
                            "...it's possible to animate images created with TextureRegion only", entityId);
                    components.remove(entityId);
                    return;
                }

                TextureRegionDrawable textureRegionDrawable = (TextureRegionDrawable) image.getDrawable();
                float delta = CommonApp.ARTEMIS_ENGINE.getDelta();
                TextureRegion keyFrame = animation2dComponent.animation.getKeyFrame(
                        animation2dComponent.stateTime, false);
                textureRegionDrawable.setRegion(keyFrame);
                animation2dComponent.stateTime += delta;
                LOG.debug("Entity {} animation stateTime is {} ", entityId, animation2dComponent.stateTime);

            } else {
                LOG.error("Animation could not be applied to [{}]", NameManager.getSysName(entityId));
                components.remove(entityId);
            }
        }

    }
}
