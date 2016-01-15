package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class Box2dBodyComponent extends Component {
    public Body body;
    /** Fixes actor2d relative rotation, use Group (parent for actual Actor) for this case */
    @Deprecated
    public int addAngleDegrees;
}
