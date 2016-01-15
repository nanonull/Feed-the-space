package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;

import java.util.UUID;

public class PlanetBaseComp extends Component {

    public UUID planetUuid;
    public int planetPosition;
    public ProgressBar healthBar;
    public RaceComp.Race hurtByNotAlly;
    public Image flag;
}
