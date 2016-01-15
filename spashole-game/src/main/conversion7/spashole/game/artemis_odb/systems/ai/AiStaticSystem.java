package conversion7.spashole.game.artemis_odb.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiFindEnemyHelper;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiStaticAttackHelper;
import org.slf4j.Logger;

public class AiStaticSystem extends IntervalEntityProcessingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();
    public static ComponentMapper<AiStaticComponent> components;
    public static final float DELTA_STEP = 0.3f;

    public AiStaticSystem() {
        super(Aspect.all(AiStaticComponent.class), DELTA_STEP);
    }

    @Override
    protected void process(Entity entity) {
        int enemyE = AiFindEnemyHelper.step(entity);

        if (enemyE > AiFindEnemyHelper.ENTITY_NOT_FOUND) {
            AiStaticAttackHelper.step(entity, enemyE);
        }
    }
}