package conversion7.spashole.game.artemis_odb.systems.time;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.DestroyEntitySystem;
import org.slf4j.Logger;

public class SchedulingSystem extends IntervalEntityProcessingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();
    public static ComponentMapper<SchedulingComponent> components;
    private long previousTimeMillis;
    private long millisDelta;

    public SchedulingSystem() {
        super(Aspect.all(SchedulingComponent.class), 0.05f);
    }

    /** Separate entity */
    public static SchedulingComponent schedule(int millisDelay, Runnable runnable) {
        return schedule(millisDelay, runnable, SpasholeApp.ARTEMIS_ENGINE.create());
    }

    /** Be careful: by default entity will be removed after scheduling executed */
    public static SchedulingComponent schedule(int millisDelay, Runnable runnable, int entity) {
        SchedulingComponent schedulingComponent = components.create(entity);
        schedulingComponent.runnable = runnable;
        schedulingComponent.delayTimeMillis = millisDelay;
        return schedulingComponent;
    }

    // TODO separate system based on step-system
    public static SchedulingComponent scheduleOnNextStep(Runnable run) {
        return schedule(0, run, SpasholeApp.ARTEMIS_ENGINE.create());
    }

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
        SchedulingComponent schedulingComponent = components.get(e);
        schedulingComponent.collectedTimeMillis += millisDelta;

        if (schedulingComponent.collectedTimeMillis >= schedulingComponent.delayTimeMillis) {
            schedulingComponent.runnable.run();
            if (schedulingComponent.killEntity) {
                DestroyEntitySystem.components.create(e.getId());
            } else {
                components.remove(e);
            }
        }
    }
}