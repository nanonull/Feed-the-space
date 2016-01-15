package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.artemis.Entity;

public class ActivationTriggerComponent extends Component {
    public Runnable runnable;
    public boolean triggerAndRemove = true;
    public Entity activatedBy;
}
