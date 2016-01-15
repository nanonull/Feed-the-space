package conversion7.spashole.game.artemis_odb;

import com.artemis.Aspect;
import com.artemis.EntitySubscription;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.Actor2dComponent;
import conversion7.spashole.game.artemis_odb.systems.AsteroidComponent;
import conversion7.spashole.game.artemis_odb.systems.ExplosiveComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.ShipComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetComponent;

/** Could not store in system classes, because static.init invoked before artemis engine is ready */
public class Aspects {
    public static final EntitySubscription BOX2D_BODIES = SpasholeApp.ARTEMIS_SUBSRIPTIONS
            .get(Aspect.all(Box2dBodyComponent.class));
    public static final EntitySubscription POSITIONS = SpasholeApp.ARTEMIS_SUBSRIPTIONS
            .get(Aspect.all(Position2dComponent.class));
    public static final EntitySubscription ACTORS = SpasholeApp.ARTEMIS_SUBSRIPTIONS
            .get(Aspect.all(Actor2dComponent.class));

    public static final EntitySubscription PLANETS = SpasholeApp.ARTEMIS_SUBSRIPTIONS
            .get(Aspect.all(PlanetComponent.class));
    public static final EntitySubscription EXPLOSIVES = SpasholeApp.ARTEMIS_SUBSRIPTIONS
            .get(Aspect.all(ExplosiveComponent.class));
    public static final EntitySubscription SHIPS = SpasholeApp.ARTEMIS_SUBSRIPTIONS
            .get(Aspect.all(ShipComponent.class));
    public static final EntitySubscription ASTEROIDS = SpasholeApp.ARTEMIS_SUBSRIPTIONS
            .get(Aspect.all(AsteroidComponent.class));

    public static void init() {

    }
}
