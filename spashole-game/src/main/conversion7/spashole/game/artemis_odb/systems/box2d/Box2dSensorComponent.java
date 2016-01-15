package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.UUID;
import java.util.function.Predicate;

public class Box2dSensorComponent extends Component {
    public ObjectSet<UUID> detectedEntities = new ObjectSet<>();
    /** Input integer is triggered entity id */
    public Predicate<Integer> startTrigger;
    /** Input integer is triggered entity id */
    public Predicate<Integer> endTrigger;
}
