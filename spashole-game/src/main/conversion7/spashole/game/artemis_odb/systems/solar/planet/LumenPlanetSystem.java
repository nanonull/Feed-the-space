package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.DestroyEntitySystem;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;

import java.util.UUID;

public class LumenPlanetSystem extends IntervalEntityProcessingSystem {
    private static final float DELTA = (DebugFlags.NORM_PLANET_CONSTR_SPEED ? 5f : 1f);
    public ComponentMapper<LumenPlanetConstructionComp> components;

    public LumenPlanetSystem() {
        super(Aspect.all(PlanetComponent.class, RaceComp.class), DELTA);
    }

    @Override
    protected void process(Entity planetE) {
        RaceComp raceComp = RaceManager.components.get(planetE);
        if (raceComp.race == RaceComp.Race.LUMEN) {
            // build
            LumenPlanetConstructionComp lumenPlanetConstructionComp = components.getSafe(planetE);
            if (lumenPlanetConstructionComp != null) {
                continueConstruction(planetE, lumenPlanetConstructionComp);
            } else {
                startConstruction(planetE);
            }
        }
    }

    private void startConstruction(Entity planetE) {
        PlanetComponent planetComponent = PlanetManager.planetComponents.get(planetE);
        int foundFreePos = -1;
        for (int i = 0; i < planetComponent.planetEntities.length; i++) {
            UUID entityU = planetComponent.planetEntities[i];
            if (entityU == null) {
                foundFreePos = i;
                break;
            }
            Entity entity = SpasholeApp.ARTEMIS_UUIDS.getEntity(entityU);
            if (entity == null
                    || DestroyEntitySystem.components.has(entity)) {
                // TODO validate planetEntities on entity destroy
                planetComponent.planetEntities[i] = null;
                foundFreePos = i;
                break;
            }
        }

        if (foundFreePos > -1) {
            LumenPlanetConstructionComp planetConstructionComp = components.create(planetE);
            planetConstructionComp.position = foundFreePos;
        }
    }

    private void continueConstruction(Entity planetE
            , LumenPlanetConstructionComp planetConstructionComp) {
        PlanetManager.addStaticGun(
                planetE.getId()
                , Actor2dManager.components.get(planetE).actor.getStage()
                , planetConstructionComp.position
        );
        components.remove(planetE);
    }
}