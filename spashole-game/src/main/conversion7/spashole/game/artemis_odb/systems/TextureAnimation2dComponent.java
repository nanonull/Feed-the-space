package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class TextureAnimation2dComponent extends Component {
    public float stateTime;
    public Animation animation;
    public Mode mode;

    public enum Mode {
        LOOP_FROM_START
        , STOP_ON_END
        , DESTROY_ENTITY_ON_END
    }
}
