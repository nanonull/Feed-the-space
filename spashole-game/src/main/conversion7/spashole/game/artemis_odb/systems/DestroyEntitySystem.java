package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import conversion7.gdxg.core.CommonApp;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dAdditionalBodiesComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dAdditionalBodiesManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.utils.SpasholeUtils;

public class DestroyEntitySystem extends IteratingSystem {

    public static ComponentMapper<DestroyEntityComponent> components;
    private static final int WARP_ENGINE_CHANCE = (DebugFlags.RANDOM_WARP_ENGINE ? 5 : 100);

    public DestroyEntitySystem() {
        super(Aspect.all(DestroyEntityComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.getSafe(entityId);
        Actor2dComponent actor2dComponent = Actor2dManager.components.getSafe(entityId);

        ShipComponent shipComponent = ShipManager.components.getSafe(entityId);
        if (shipComponent != null) {
            RaceComp raceComp = RaceManager.components.getSafe(entityId);
            if (raceComp != null && raceComp.race == RaceComp.Race.GROK) {
                Vector2 position = box2dBodyComponent.body.getPosition();
                if (SpasholeUtils.RANDOM.nextInt(100) < WARP_ENGINE_CHANCE) {
                    InventoryItemManager.create(InventoryItemComp.Type.WARP_ENGINE
                            , position.x, position.y, actor2dComponent.actor.getStage());
                }
            }
        }

        if (box2dBodyComponent != null) {
            SpasholeApp.box2dWorld.destroyBody(box2dBodyComponent.body);
        }

        if (actor2dComponent != null) {
            actor2dComponent.actor.remove();
        }

        Box2dAdditionalBodiesComponent additionalBodiesComponent =
                Box2dAdditionalBodiesManager.components.getSafe(entityId);
        if (additionalBodiesComponent != null) {
            for (Body body : additionalBodiesComponent.bodies) {
                SpasholeApp.box2dWorld.destroyBody(body);
            }
        }

        CommonApp.ARTEMIS_ENGINE.delete(entityId);
    }
}
