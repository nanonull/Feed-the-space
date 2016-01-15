package conversion7.spashole.game.artemis_odb.systems.ai;

import com.artemis.Component;
import conversion7.spashole.game.SpasholeApp;

import java.util.UUID;

public class AiMoveToEntityComp extends Component {

    public UUID moveTo;
    public float distanceToReach;

    public int getTargetId() {
        return SpasholeApp.ARTEMIS_UUIDS.getEntity(moveTo).getId();
    }
}
