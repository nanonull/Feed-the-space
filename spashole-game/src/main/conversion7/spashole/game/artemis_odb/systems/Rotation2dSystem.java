package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class Rotation2dSystem extends IteratingSystem {

    public static ComponentMapper<Rotation2dComponent> components;

    public Rotation2dSystem() {
        super(Aspect.all(Rotation2dComponent.class, Actor2dComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Rotation2dComponent rotation2dComponent = components.get(entityId);
        Actor2dComponent actor2dComponent = Actor2dManager.components.get(entityId);
        actor2dComponent.actor.setRotation(rotation2dComponent.angleDegrees);
    }
}
