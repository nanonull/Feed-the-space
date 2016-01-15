package conversion7.spashole.game.artemis_odb.systems.time;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import org.slf4j.Logger;

public class IntervalExecutorSystem extends SelfIntervalEntityExecutorSystem {
    private static final Logger LOG = Utils.getLoggerForClass();
    private static ComponentMapper<IntervalExecutorComponent> components;
    private static final float DELTA = 0.05f;

    public IntervalExecutorSystem() {
        super(Aspect.all(IntervalExecutorComponent.class), DELTA);
    }

    public ComponentMapper<? extends IntervalExecutorComponent> getComponents() {
        return components;
    }

    /** Separate entity */
    public static IntervalExecutorComponent schedule(int intervalMillis, Runnable runnable) {
        return schedule(intervalMillis, runnable, SpasholeApp.ARTEMIS_ENGINE.create());
    }

    public static IntervalExecutorComponent schedule(int intervalMillis, Runnable runnable, int entity) {
        IntervalExecutorComponent schedulingComponent = components.create(entity);
        schedulingComponent.runnable = runnable;
        schedulingComponent.intervalMillis = intervalMillis;
        schedulingComponent.entityUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(
                SpasholeApp.ARTEMIS_ENGINE.getEntity(entity));
        return schedulingComponent;
    }

}