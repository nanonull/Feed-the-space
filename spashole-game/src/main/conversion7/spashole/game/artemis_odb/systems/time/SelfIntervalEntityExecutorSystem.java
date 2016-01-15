package conversion7.spashole.game.artemis_odb.systems.time;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;

/**
 * This system expects user define runnable code per entity.<br>
 * Also system interval means only "how often to accumulate and check intervals of each entity" -
 * define less possible interval for system,
 * because it will affect time accuracy of entity runnable code execution.
 */
public abstract class SelfIntervalEntityExecutorSystem extends IntervalEntityProcessingSystem {
    private long previousTimeMillis;
    private long millisDelta;

    /**
     * @param intervalForComponentsUpdate in seconds between process components with runnable code. But... this doesn't mean runnable code will be executed each system#process.<br>
     *                                    Example: system has interval 1 sec, component has interval 2 sec. Component's runnable will be executed each 2nd system#process.<br>
     */
    public SelfIntervalEntityExecutorSystem(Aspect.Builder aspect, float intervalForComponentsUpdate) {
        super(aspect, intervalForComponentsUpdate);
    }

    public abstract ComponentMapper<? extends SelfIntervalExecutorComponent> getComponents();

    @Override
    protected void processSystem() {
        long currentTimeMillis = System.currentTimeMillis();

        if (previousTimeMillis != 0) {
            millisDelta = currentTimeMillis - previousTimeMillis;
            super.processSystem();
        }

        previousTimeMillis = currentTimeMillis;
    }

    @Override
    protected void process(Entity e) {
        SelfIntervalExecutorComponent intervalExecutorComponent = getComponents().get(e);
        intervalExecutorComponent.collectedTimeMillis += millisDelta;

        if (intervalExecutorComponent.collectedTimeMillis >= intervalExecutorComponent.intervalMillis) {
            intervalExecutorComponent.collectedTimeMillis -= intervalExecutorComponent.intervalMillis;
            intervalExecutorComponent.runnable.run();
            if (intervalExecutorComponent.stop) {
                getComponents().remove(e);
            }
        }
    }
}
