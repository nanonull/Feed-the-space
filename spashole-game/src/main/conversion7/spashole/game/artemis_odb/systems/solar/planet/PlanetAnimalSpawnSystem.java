package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.AnimalManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.ShipManager;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalEntityExecutorSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalExecutorComponent;
import org.slf4j.Logger;

import java.util.UUID;

public class PlanetAnimalSpawnSystem extends SelfIntervalEntityExecutorSystem {
    private static final Logger LOG = Utils.getLoggerForClass();
    public static ComponentMapper<PlanetAnimalSpawnComp> components;
    private static final float DELTA = 0.3f;
    public static final int SPAWN_LIMIT = 5;

    public PlanetAnimalSpawnSystem() {
        super(Aspect.all(PlanetComponent.class, PlanetAnimalSpawnComp.class), DELTA);
    }

    @Override
    public ComponentMapper<? extends SelfIntervalExecutorComponent> getComponents() {
        return components;
    }

    public static UUID spawn(Entity planet) {
        if (!DebugFlags.SPAWN_ANIMAL) {
            LOG.info("DebugFlags.SPAWN_ANIMAL OFF");
            return null;
        }
        Position2dComponent planetPosition2dComponent = Position2dSystem.components.get(planet);
        Stage stage = Actor2dManager.components.get(planet).actor.getStage();
        Vector2 onCircle = MathUtilsExt.getPositionOnCircle(MathUtilsExt.randomRadian()
                , PlanetManager.PLANET_RADIUS_WORLD + ShipManager.SHIP_RADIUS_WORLD * 2);
        onCircle.add(planetPosition2dComponent.pos.x, planetPosition2dComponent.pos.y);

        RaceComp.Race race = RaceManager.components.get(planet).race;
        Entity animal = AnimalManager.create(
                onCircle.x, onCircle.y
                , race
                , stage
        );

        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(animal);
        PlanetAnimalSpawnComp animalSpawnComp = components.get(planet);
        animalSpawnComp.spawnedEntities.add(uuid);

        return uuid;
    }

    public static void step(Entity planet) {
        PlanetAnimalSpawnComp animalSpawnComp = components.get(planet);
        animalSpawnComp.validateAliveSpawnedEntities();
        if (animalSpawnComp.spawnedEntities.size < PlanetAnimalSpawnSystem.SPAWN_LIMIT) {
            spawn(planet);
        }
    }

    @Override
    protected void process(Entity e) {
        RaceComp.Race race = RaceManager.components.get(e).race;
        if (race == RaceComp.Race.ANIMAL_AGGR
                || race == RaceComp.Race.ANIMAL_NEUT) {
            super.process(e);
        }
    }
}