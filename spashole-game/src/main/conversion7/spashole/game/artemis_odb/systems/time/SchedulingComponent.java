package conversion7.spashole.game.artemis_odb.systems.time;

import com.artemis.Component;

public class SchedulingComponent extends Component {
    public Runnable runnable;
    float collectedTimeMillis;
    public float delayTimeMillis;
    public boolean killEntity = true;
}