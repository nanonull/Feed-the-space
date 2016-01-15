package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Entity;
import com.badlogic.gdx.utils.ObjectSet;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.Tags;
import conversion7.spashole.game.artemis_odb.systems.solar.SolarSystem;
import conversion7.spashole.game.artemis_odb.systems.time.IntervalExecutorSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalExecutorComponent;
import conversion7.spashole.game.ui.CentralPanel;

import java.util.UUID;

public class PlayerInfoUpdateSystem {
    public static SelfIntervalExecutorComponent panelRefresher;
    public static ObjectSet<UUID> visiblePlanets = new ObjectSet<>();
    static Entity solarSystemChecker;
    private static boolean solarWarnShown = false;

    private static final Runnable CLEAR_SOLAR_BOUNDS_WARN = () -> {
        CentralPanel centralPanel = SpasholeApp.ui.getCentralPanel();
        centralPanel.hide();
        solarWarnShown = false;
    };

    public static void scheduleUpdate() {
        solarSystemChecker = SpasholeApp.ARTEMIS_ENGINE.createEntity();

        panelRefresher = IntervalExecutorSystem.schedule(250, () -> {
            Entity controlledEntity = SpasholeApp.ARTEMIS_TAGS.getEntity(Tags.PLAYER_ENTITY);
            if (controlledEntity == null) return;
            SpasholeApp.ui.getPlayerStatePanel().refresh(controlledEntity);
            if (!SolarSystem.checkInSolarSystem(controlledEntity)
                    && !solarWarnShown) {
                CentralPanel centralPanel = SpasholeApp.ui.getCentralPanel();
                centralPanel.refresh("You leave solar system!");
                centralPanel.show();
                solarWarnShown = true;
                SchedulingSystem.schedule(2000, CLEAR_SOLAR_BOUNDS_WARN, solarSystemChecker.getId()).killEntity = false;
            }
        });
    }

}
