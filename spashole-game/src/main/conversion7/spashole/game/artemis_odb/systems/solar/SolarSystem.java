package conversion7.spashole.game.artemis_odb.systems.solar;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.Aspects;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetComponent;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetManager;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;
import org.slf4j.Logger;

public class SolarSystem extends IntervalEntityProcessingSystem {
    private static final Logger LOG = Utils.getLoggerForClass();

    public static final float DELTA_STEP = 0.05f;
    private static Vector2 sunPosition;
    private static Vector2 sunToPlanetWip = new Vector2();
    private static final float MOVE_BODY_STEP =
            (DebugFlags.PLANET_NORMAL_MOVE_SPEED
                    ? DELTA_STEP * 0.008f
                    : DELTA_STEP * 0.1f);
    private static final float ROTATE_BODY_STEP =
            (DebugFlags.PLANET_NORMAL_ROTATION_SPEED
                    ? DELTA_STEP * MathUtilsExt.toRadians(0.01f)
                    : DELTA_STEP * MathUtilsExt.toRadians(0.1f));
    public static float SYSTEM_RADIUS;
    static Vector2 solarSysEnd;

    public SolarSystem() {
        super(Aspect.all(PlanetComponent.class), DELTA_STEP);
    }

    public static void setSunEntity(int sunEntity) {
        Box2dBodyComponent sunBody = Box2dBodySystem.components.get(sunEntity);
        sunPosition = sunBody.body.getPosition();
    }

    public static void postInit() {
        SYSTEM_RADIUS = solarSysEnd.dst(sunPosition);
        SchedulingSystem.schedule(500, () -> {
            IntBag planets = Aspects.PLANETS.getEntities();
            LOG.info("PlanetDataPopulating, planets: {}", planets.size());

            Position2dComponent sunPos = Position2dSystem.components.get(
                    SpasholeApp.ARTEMIS_UUIDS.getEntity(SunManager.sunEntityUuid));

            Array<PlanetComponent> planetComponents = new Array<>();
            for (int i = 0; i < planets.size(); i++) {
                int planetId = planets.get(i);
                PlanetComponent planetComponent = PlanetManager.planetComponents.get(planetId);
                planetComponents.add(planetComponent);
                planetComponent.name = NameManager.getName(planetId);

                Position2dComponent planPos = Position2dSystem.components.get(planetId);
                planetComponent.dstFromSun = Vector2.dst(
                        planPos.pos.x, planPos.pos.y
                        , sunPos.pos.x, sunPos.pos.y
                );
            }

            planetComponents.sort((o1, o2) -> Float.compare(o1.dstFromSun, o2.dstFromSun));
            for (int i = 0; i < planetComponents.size; i++) {
                PlanetComponent planetComponent = planetComponents.get(i);
                planetComponent.positionInSolarSystem = i + 1;
                planetComponent.infoPanel.refresh(planetComponent.name, planetComponent.positionInSolarSystem);
            }
        });
    }

    public static boolean checkInSolarSystem(Entity entity) {
        Entity sunE = SpasholeApp.ARTEMIS_UUIDS.getEntity(SunManager.sunEntityUuid);
        Position2dComponent sunPos = Position2dSystem.components.get(sunE);
        Position2dComponent entPos = Position2dSystem.components.get(entity);
        return sunPos.pos.dst(entPos.pos) < SYSTEM_RADIUS;
    }

    public static void setEnd(float worldX, float worldY) {
        solarSysEnd = new Vector2(worldX, worldY);
    }

    @Override
    protected void process(Entity entity) {
        Box2dBodyComponent planetBody = Box2dBodySystem.components.get(entity);

        Vector2 planetPos = planetBody.body.getPosition();
        sunToPlanetWip.set(planetPos).sub(sunPosition);
        float angleRadToTarget = sunToPlanetWip.angleRad();
        float sunToPlanetDst = sunToPlanetWip.len();
        Vector2 nextPos = MathUtilsExt.getPositionOnCircle(
                MathUtilsExt.absRadians(angleRadToTarget + MOVE_BODY_STEP)
                , sunToPlanetDst);
        // next position of planet in world
        nextPos.add(sunPosition);
        // get diff for move
        nextPos.sub(planetPos);

        nextPos.scl(10);
        Box2dBodySystem.applyForceToMoveBy(nextPos.x, nextPos.y, planetBody.body);
        if (DebugFlags.PLANET_ROTATION_ENABLED) {
            Box2dBodySystem.applyTorqueToRotateBy(ROTATE_BODY_STEP, planetBody.body);
        } else {
            LOG.info("DebugFlags.PLANET_ROTATION_ENABLED OFF");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Planet [{}] distance: {}"
                    , NameManager.getSysName(entity.getId())
                    , sunToPlanetDst);
        }
    }
}
