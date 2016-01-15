package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;

import java.util.UUID;

public class ShipComponent extends Component {

    public UUID pilotUuid;
    public boolean engineWorks = true;
    public boolean evasionAvailable = true;
    public boolean pilotable = false;
    public Image shipImg;
    public Image gunImage;

    public void usedEvasion(int entId) {
        evasionAvailable = false;
        SchedulingSystem.schedule(1000, ()->{
            evasionAvailable = true;
        }, entId).killEntity = false;
    }
}
