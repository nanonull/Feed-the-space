package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;
import conversion7.spashole.game.artemis_odb.systems.utils.ShotRunnable;

public class EntityExitsCameraViewTriggerComponent extends Component {
    public ObjectMap<String, ShotRunnable> runnables = new ObjectMap<>();

    public void addRunnable(boolean singleShot, String key, Runnable runnable) {
        ShotRunnable entry = new ShotRunnable(singleShot,runnable);
        runnables.put(key, entry);
    }


}
