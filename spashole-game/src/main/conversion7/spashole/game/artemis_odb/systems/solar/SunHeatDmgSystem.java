package conversion7.spashole.game.artemis_odb.systems.solar;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalEntityExecutorSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalExecutorComponent;

public class SunHeatDmgSystem extends SelfIntervalEntityExecutorSystem {
    public static ComponentMapper<SunHeatDmgComponent> components;
    private static final float DELTA = 0.2f;

    public SunHeatDmgSystem() {
        super(Aspect.all(SunHeatDmgComponent.class), DELTA);
    }

    @Override
    public ComponentMapper<? extends SelfIntervalExecutorComponent> getComponents() {
        return components;
    }
}