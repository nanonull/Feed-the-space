package conversion7.spashole.game.artemis_odb.systems.box2d;

import com.artemis.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Box2dAdditionalBodiesComponent extends Component {
    public Array<Body> bodies = new Array<>();
}
