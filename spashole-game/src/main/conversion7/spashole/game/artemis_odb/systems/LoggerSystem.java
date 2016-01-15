package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import conversion7.gdxg.core.utils.Utils;
import org.slf4j.Logger;

public class LoggerSystem extends IteratingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static ComponentMapper<LoggerComponent> components;

    public LoggerSystem() {
        super(Aspect.all(LoggerComponent.class));
    }

    @Override
    protected void process(int entityId) {
        LoggerComponent component = components.get(entityId);
        LOG.info(component.getLogMessage());

    }
}
