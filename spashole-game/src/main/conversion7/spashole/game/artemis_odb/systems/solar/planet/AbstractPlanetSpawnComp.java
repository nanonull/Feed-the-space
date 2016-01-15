package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.Entity;
import com.badlogic.gdx.utils.ObjectSet;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.time.SelfIntervalExecutorComponent;

import java.util.UUID;

public class AbstractPlanetSpawnComp extends SelfIntervalExecutorComponent {
    public final ObjectSet<UUID> spawnedEntities = new ObjectSet<>();

    public void validateAliveSpawnedEntities() {
        ObjectSet.ObjectSetIterator<UUID> iterator = spawnedEntities.iterator();
        while (iterator.hasNext()) {
            UUID next = iterator.next();
            Entity entity = SpasholeApp.ARTEMIS_UUIDS.getEntity(next);
            if (entity == null) {
                iterator.remove();
            }
        }
    }

}
