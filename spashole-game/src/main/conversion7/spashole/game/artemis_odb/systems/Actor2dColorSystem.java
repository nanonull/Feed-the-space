package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class Actor2dColorSystem  extends IteratingSystem {
    public static ComponentMapper<Actor2dColorComponent> components;

    public Actor2dColorSystem() {
        super(Aspect.all(Actor2dColorComponent.class, Actor2dComponent.class));
    }

    @Override
    protected void process(int entityId) {
        Actor2dColorComponent colorComponent = components.get(entityId);
        Actor2dComponent actor2dComponent = Actor2dManager.components.get(entityId);
        actor2dComponent.actor.setColor(colorComponent.color);
        components.remove(entityId);
    }
}