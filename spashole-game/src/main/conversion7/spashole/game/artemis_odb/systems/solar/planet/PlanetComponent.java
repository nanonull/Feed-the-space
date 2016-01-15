package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.artemis.Component;
import com.artemis.Entity;

import java.util.UUID;

public class PlanetComponent extends Component {
    public final UUID[] planetEntities = new UUID[PlanetManager.PLANET_POSITIONS];
    public PlanetBaseComp baseComponent;
    public Entity baseEntity;

    public PlanetInfoPanel infoPanel;
    public float dstFromSun;
    public String name;
    public int positionInSolarSystem;
}
