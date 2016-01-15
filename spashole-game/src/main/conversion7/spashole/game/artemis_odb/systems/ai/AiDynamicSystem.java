package conversion7.spashole.game.artemis_odb.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.artemis_odb.Aspects;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiDynamicAttackHelper;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiFindEnemyHelper;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiWalkAroundHelper;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetManager;
import org.slf4j.Logger;

import java.util.function.Function;

public class AiDynamicSystem extends IntervalEntityProcessingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();
    public static ComponentMapper<AiDynamicComponent> components;
    public static final float DELTA_STEP = 0.3f;
    private static final float MOVE_TO_PLANET_DST = PlanetManager.PLANET_RADIUS_WORLD * 2;
    private Vector2 DST_WIP = new Vector2();
    Vector2 dstWip = new Vector2();

    public AiDynamicSystem() {
        super(Aspect.all(AiDynamicComponent.class), DELTA_STEP);
    }

    public static AiDynamicComponent create(int entity) {
        return components.create(entity);
    }

    @Override
    protected void process(Entity entity) {
        int enemyE = AiFindEnemyHelper.step(entity);

        if (enemyE > AiFindEnemyHelper.ENTITY_NOT_FOUND) {
            AiDynamicAttackHelper.step(entity, enemyE);

        } else {
            AiMoveToEntityComp aiMoveToEntityComp = AiDynamicMoveToEntityManager.components.getSafe(entity);
            if (aiMoveToEntityComp != null) {
                if (AiDynamicMoveToEntityManager.isNotCompleted(entity.getId(), aiMoveToEntityComp)) {
                    AiDynamicMoveToEntityManager.step(entity.getId(), aiMoveToEntityComp.getTargetId());
                    return;
                }
            }

            // TODO could AiMoveToEntityComp be used instead?
            AiGrokComp aiGrokComp = AiGrokManager.components.getSafe(entity);
            if (aiGrokComp != null) {
                // grok AI
                if (aiGrokComp.hasTask()) {
                    if (validateTask(aiGrokComp, entity)) {
                        AiDynamicMoveToEntityManager.step(entity.getId(), aiGrokComp.moveToPlanet);
                    }
                    return;

                } else {
                    int nearestPlanet = findNearestPlanet(
                            race -> race != RaceComp.Race.GROK
                            , entity);
                    if (nearestPlanet > -1) {
                        aiGrokComp.moveToPlanet = nearestPlanet;
                        return;
                    }
                }

                // groks win?
            }

            AiWalkAroundHelper.step(entity.getId());
        }
    }

    private boolean validateTask(AiGrokComp aiGrokComp, Entity entity) {
        RaceComp raceComp = RaceManager.components.getSafe(aiGrokComp.moveToPlanet);
        if (raceComp != null && raceComp.race != RaceComp.Race.GROK) {
            Position2dComponent aiObjPos = Position2dSystem.components.get(entity);
            Position2dComponent planetPos = Position2dSystem.components.get(aiGrokComp.moveToPlanet);
            float dst = dstWip.set(aiObjPos.pos).dst(planetPos.pos);
            if (dst > MOVE_TO_PLANET_DST) {
                return true;
            }
        }

        // completed
        aiGrokComp.moveToPlanet = -1;
        return false;
    }

    private int findNearestPlanet(Function<RaceComp.Race, Boolean> raceFunc, Entity entity) {
        Position2dComponent positionOfE = Position2dSystem.components.get(entity);
        float bestDst = Float.MAX_VALUE;
        int bestId = -1;
        IntBag planets = Aspects.PLANETS.getEntities();
        for (int i = 0; i < planets.size(); i++) {
            int planetId = planets.get(i);
            RaceComp raceComp = RaceManager.components.get(planetId);
            if (raceFunc.apply(raceComp.race)) {
                Position2dComponent position2dComponent = Position2dSystem.components.get(planetId);
                float dst = DST_WIP.set(position2dComponent.pos).sub(positionOfE.pos).len();
                if (dst < bestDst) {
                    bestDst = dst;
                    bestId = planetId;
                }
            }
        }

        return bestId;
    }
}