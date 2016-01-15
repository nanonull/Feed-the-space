package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import conversion7.gdxg.core.dialog.AbstractDialog;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.EntityEntersCameraViewTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.ShipManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.AiGrokManager;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiFindEnemyHelper;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalEntityExecutorSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalExecutorComponent;
import conversion7.spashole.game.story.Scenario;
import conversion7.spashole.game.story.dialogs.GrokShipFirstDialog;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;

import java.util.UUID;

public class PlanetShipSpawnSystem extends SelfIntervalEntityExecutorSystem {

    private static final Logger LOG = Utils.getLoggerForClass();
    public static ComponentMapper<PlanetShipSpawnComp> components;
    private static final float DELTA = 0.3f;
    public static final int SPAWN_LIMIT = (DebugFlags.NORM_SPAWN_SHIPS_LIMIT ? 3 : 1);

    public PlanetShipSpawnSystem() {
        super(Aspect.all(PlanetComponent.class, PlanetShipSpawnComp.class), DELTA);
    }

    @Override
    public ComponentMapper<? extends SelfIntervalExecutorComponent> getComponents() {
        return components;
    }

    public static UUID spawn(Entity planet) {
        if (!DebugFlags.SPAWN_SHIPS) {
            LOG.info("DebugFlags.SPAWN_SHIPS OFF");
            return null;
        }
        Position2dComponent planetPosition2dComponent = Position2dSystem.components.get(planet);
        Stage stage = Actor2dManager.components.get(planet).actor.getStage();

        Vector2 onCircle = MathUtilsExt.getPositionOnCircle(MathUtilsExt.randomRadian()
                , PlanetManager.PLANET_RADIUS_WORLD + ShipManager.SHIP_RADIUS_WORLD * 2);
        onCircle.add(planetPosition2dComponent.pos.x, planetPosition2dComponent.pos.y);

        Entity ship = ShipManager.createShip(onCircle.x, onCircle.y, stage
                , RaceManager.components.get(planet).race == RaceComp.Race.GROK);
        UUID shipUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(ship);

        RaceComp shipRace = RaceManager.components.create(ship);
        shipRace.race = RaceManager.components.get(planet).race;

        AiDynamicSystem.create(ship.getId());
        AiFindEnemyHelper.createViewSensor(ship, SpasholeUtils.sceneToWorld(550f));
        if (shipRace.race == RaceComp.Race.GROK) {
            AiGrokManager.components.create(ship);
            if (!Scenario.GrokGlobals.isPlayerSawGrokShip()) {
                EntityEntersCameraViewTriggerManager.componentCreate(ship.getId())
                        .addRunnable(true, "Scenario.GrokGlobals.isPlayerSawGrokShip", () -> {
                            // 2nd check for already created ships
                            if (!Scenario.GrokGlobals.isPlayerSawGrokShip()) {
                                AbstractDialog.start(new GrokShipFirstDialog(ship, SpasholeApp.ui.getDialogWindow()));
                            }
                        });
            }
        }

        PlanetShipSpawnComp shipSpawnComp = components.get(planet);
        shipSpawnComp.spawnedEntities.add(shipUuid);

        return shipUuid;
    }

    public static void step(Entity planet) {
        PlanetShipSpawnComp shipSpawnerComp = components.get(planet);
        shipSpawnerComp.validateAliveSpawnedEntities();
        if (shipSpawnerComp.spawnedEntities.size < PlanetShipSpawnSystem.SPAWN_LIMIT) {
            PlanetShipSpawnSystem.spawn(planet);
        }
    }

    @Override
    protected void process(Entity e) {
        if (RaceManager.components.get(e).race == RaceComp.Race.GROK) {
            super.process(e);
        }
    }
}