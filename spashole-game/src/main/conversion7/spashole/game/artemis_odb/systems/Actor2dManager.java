package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Actor2dManager extends Manager {

    public static ComponentMapper<Actor2dComponent> components;

    public static void adjustPositionCenterBySize(Actor actor) {
        adjustPositionCenterBySize(actor, true);
    }

    public static void adjustPositionCenterBySize(Actor actor, boolean orientation) {
        if (orientation) {
            actor.setPosition(actor.getWidth() / -2, actor.getHeight() / -2);
        } else {
            actor.setPosition(actor.getHeight() / -2, actor.getWidth() / -2);
        }
    }
}
