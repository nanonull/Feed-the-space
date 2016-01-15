package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import org.slf4j.Logger;

public class DisableEntitySystem extends IteratingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static ComponentMapper<DisableEntityComponent> components;

    public DisableEntitySystem() {
        super(Aspect.all(DisableEntityComponent.class));
    }

    @Override
    protected void process(int entityId) {
        LOG.info("Disable {}", NameManager.getSysName(entityId));

        if (Box2dBodySystem.components.has(entityId)) {
            Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.get(entityId);

            for (Fixture fixture : box2dBodyComponent.body.getFixtureList()) {
                Filter filter = fixture.getFilterData();
                filter.categoryBits = Box2dBodySystem.INACTIVE_BODY;
                filter.maskBits = Box2dBodySystem.NOBODY_MASK;
                fixture.setFilterData(filter);
            }
        }

        if (Actor2dManager.components.has(entityId)) {
            Actor2dComponent actor2dComponent = Actor2dManager.components.get(entityId);
            actor2dComponent.previousParent = actor2dComponent.actor.getParent();
            actor2dComponent.actor.remove();
        }

        components.remove(entityId);
    }
}