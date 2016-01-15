package conversion7.spashole.game.artemis_odb.systems.ai;

import com.artemis.Component;

public class AiGrokComp extends Component {

    public int moveToPlanet = -1;

    public boolean hasTask() {
        return moveToPlanet > -1;
    }
}
