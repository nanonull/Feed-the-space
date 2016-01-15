package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Position2dComponent extends Component {
    public Vector2 pos = new Vector2();

    @Override
    public String toString() {
        return pos.toString();
    }
}
