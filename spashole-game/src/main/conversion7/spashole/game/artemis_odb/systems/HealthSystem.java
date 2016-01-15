package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class HealthSystem extends IteratingSystem {

    public static ComponentMapper<HealthComponent> components;

    public HealthSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void process(int entityId) {
        HealthComponent healthComponent = components.get(entityId);
        if (healthComponent.health <= 0) {
            if (ExplosiveManager.components.has(entityId)
                    && Actor2dManager.components.has(entityId)) {
                ExplosiveManager.createExplosion(entityId);
            }

            if (healthComponent.onDeathTrigger != null) {
                healthComponent.onDeathTrigger.run();
            }

            DestroyEntitySystem.components.create(entityId);
        }
    }
}
