package conversion7.spashole.game.story;

import aurelienribon.tweenengine.Tween;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.gdxg.core.custom2d.ui_logger.UiLogger;
import conversion7.gdxg.core.tween.ActorAccessor;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.ResKey;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.Aspects;
import conversion7.spashole.game.artemis_odb.CommonMappers;
import conversion7.spashole.game.artemis_odb.systems.Actor2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.AsteroidSystem;
import conversion7.spashole.game.artemis_odb.systems.CameraControllerFocusSystem;
import conversion7.spashole.game.artemis_odb.systems.EntityListenEnterInCameraViewSystem;
import conversion7.spashole.game.artemis_odb.systems.EntityListenExitFromCameraViewSystem;
import conversion7.spashole.game.artemis_odb.systems.ExplosiveManager;
import conversion7.spashole.game.artemis_odb.systems.HealthComponent;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.PlayerInfoUpdateSystem;
import conversion7.spashole.game.artemis_odb.systems.PlayerInputSystem;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import conversion7.spashole.game.artemis_odb.systems.ShipManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicMoveToEntityManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.AiManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiStaticSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dCollisionTriggerComp;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dMotorJointActionSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dWorldSystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.BlackHoleGravitySystem;
import conversion7.spashole.game.artemis_odb.systems.solar.SunHeatDmgSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.SunManager;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.LumenPlanetSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetAnimalSpawnSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetShipSpawnSystem;
import conversion7.spashole.game.artemis_odb.systems.time.IntervalExecutorComponent;
import conversion7.spashole.game.artemis_odb.systems.time.IntervalExecutorSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;
import conversion7.spashole.game.audio.AudioPlayer;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;

import java.util.UUID;

public class FinalScene {

    private static final Logger LOG = Utils.getLoggerForClass();

    public static final ResKey TY_NE_NAHODISH_SYA_VO_VSELENNOI_TY_I_EST_VSELENNAYA = new ResKey("TY_NE_NAHODISH_SYA_VO_VSELENNOI_TY_I_EST_VSELENNAYA");
    public static final ResKey RASA_LUMENOV_BYLA_SPASENA_Y_AVNO_ILI_NEYAVNO_ZNAETE_I = new ResKey("RASA_LUMENOV_BYLA_SPASENA_Y_AVNO_ILI_NEYAVNO_ZNAETE_I");
    public static final ResKey NO_VSELENNAYA_NE_BYLA_IDEAL_NOI = new ResKey("NO_VSELENNAYA_NE_BYLA_IDEAL_NOI");
    public static boolean started;
    public static final ResKey SPUSTYA_NESKOL_KO_GIGASEKUND = new ResKey("SPUSTYA_NESKOL_KO_GIGASEKUND");

    /** fade out is expected before this scene */
    public static void start(Stage stage) {
        started = true;
        AudioPlayer.play(SpasholeAssets.track2Landing);
        UiLogger.infoEnabled = false;
        SchedulingSystem.schedule((DebugFlags.NORM_FINAL_TIMES ? 2000 : 500), () -> {
            SpasholeApp.ui.hideGameWindows();
            SpasholeApp.ui.show();
            SpasholeApp.ui.getFinalPanel().refresh(SpasholeAssets.textResources.get(TY_NE_NAHODISH_SYA_VO_VSELENNOI_TY_I_EST_VSELENNAYA));
            SpasholeApp.ui.getFinalPanel().show();

            SchedulingSystem.schedule((DebugFlags.NORM_FINAL_TIMES ? 8000 : 1500), () -> {
                SpasholeApp.ui.getFinalPanel().hide();

                SchedulingSystem.schedule(1000, () -> {
                    if (Scenario.GrokGlobals.isRaceAlive()) {
                        destroySolarSystemScene(stage);
                    } else {
                        savedSolarSystemScene();
                    }
                });
            });
        });
    }

    static void destroySolarSystemScene(Stage stage) {
        SpasholeApp.core.getActiveScene().fadeIn(4000);
        SchedulingSystem.schedule(2000, () -> {
            SpasholeApp.ui.getFinalPanel().refresh(SpasholeAssets.textResources.get(SPUSTYA_NESKOL_KO_GIGASEKUND));
            SpasholeApp.ui.getFinalPanel().show();
            SchedulingSystem.schedule(10000, () -> {
                SpasholeApp.ui.getFinalPanel().hide();
            });
        });

        createGrokShipWithBlackHoleBomb(stage);
    }

    private static UUID createGrokShipWithBlackHoleBomb(Stage stage) {
        AiManager.setAiState(true);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dWorldSystem.class).setEnabled(true);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dBodySystem.class).setEnabled(true);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dMotorJointActionSystem.class).setEnabled(true);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(PlayerInputSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(EntityListenEnterInCameraViewSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(EntityListenExitFromCameraViewSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(SunHeatDmgSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(LumenPlanetSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(PlanetAnimalSpawnSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(PlanetShipSpawnSystem.class).setEnabled(false);

        Entity sunE = SpasholeApp.ARTEMIS_UUIDS.getEntity(SunManager.sunEntityUuid);
        Position2dComponent sunPos = Position2dSystem.components.get(sunE);

        Vector2 shipPos = new Vector2(sunPos.pos);
        shipPos.sub(0, SunManager.SUN_BODY_RADIUS * 4);
        Entity shipE = ShipManager.createShip(shipPos.x, shipPos.y, stage, true);
        UUID shipUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(shipE);
        CameraControllerFocusSystem.setTarget(shipUuid);
        HealthComponent shipHP = HealthSystem.components.get(shipE);
        shipHP.setup(99999);
        Box2dCollisionTriggerComp collisionTriggerComp = CommonMappers.collisionTriggers.create(shipE);
        collisionTriggerComp.trigger = () -> {
            if (collisionTriggerComp.collideWith == sunE.getId()) {
                shipHP.health = 0;
                float totalSec = 3f;
                Entity blackHoleE = createBlackHole(stage, totalSec);
                SchedulingSystem.schedule((int) (totalSec * 1000), () -> {
                    startBlackHoleGravity(blackHoleE);
                });
            }
        };

        AiDynamicSystem.create(shipE.getId());
        AiDynamicMoveToEntityManager.create(shipE.getId(), SunManager.sunEntityUuid, 0);
// disable enemy searching:
        GunSystem.components.remove(shipE);

        return shipUuid;
    }

    private static Entity createBlackHole(Stage stage, float totalSec) {
        Entity sunE = SpasholeApp.ARTEMIS_UUIDS.getEntity(SunManager.sunEntityUuid);
        Position2dComponent sunPos = Position2dSystem.components.get(sunE);

        // artemis entity
        Entity blackHoleE = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        UUID effectUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(blackHoleE);
        CameraControllerFocusSystem.setTarget(effectUuid, 0);
        Position2dComponent effectPos = Position2dSystem.components.create(blackHoleE);
        effectPos.pos.set(sunPos.pos);
        Rotation2dComponent effectRotat = Rotation2dSystem.components.create(blackHoleE);
        NameManager.components.create(blackHoleE).name = "WarpSceneEffect";

        Group group = new Group();
        stage.addActor(group);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(blackHoleE);
        actor2dComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeApp.WORLD_SCALE);
        group.setZIndex(0);

        Image holeImage = new Image(SpasholeAssets.blackholeImg);
        group.addActor(holeImage);
        holeImage.setOrigin(Align.center);
        Actor2dManager.adjustPositionCenterBySize(holeImage);

        // animate
        int totalMs = (int) (totalSec * 1000);

        holeImage.setColor(1, 1, 1, 0);
        Tween.to(holeImage, ActorAccessor.COLOR_ALPHA, totalSec)
                .target(1f)
                .start(SpasholeApp.tweenManager);

        IntervalExecutorSystem.schedule(15, () -> {
            effectRotat.angleDegrees -= 0.007f;
        });

        Camera camera = CameraControllerFocusSystem.getCameraController().getCamera();
        float viewWiStep = camera.viewportWidth / 600f;
        float viewHeStep = camera.viewportHeight / 600f;
        IntervalExecutorComponent cameraViewInterval = IntervalExecutorSystem.schedule(15, () -> {
            camera.viewportWidth += viewWiStep;
            camera.viewportHeight += viewHeStep;
        });
        SchedulingSystem.schedule(20000, () -> {
                }, SpasholeApp.ARTEMIS_UUIDS.getEntity(cameraViewInterval.entityUuid).getId()
        ).killEntity = true;

        return blackHoleE;
    }

    private static void startBlackHoleGravity(Entity blackHoleE) {
        LOG.info("startBlackHoleGravity");
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dWorldSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dBodySystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dMotorJointActionSystem.class).setEnabled(false);
        // TODO clear box2d: ensure all components related to box2d are cleared!
//        Box2dWorldSystem.destroyAllBodies();

        BlackHoleGravitySystem.setBlackHole(blackHoleE);

        // add BlackHoleGravitySystem.components
        IntBag actors = Aspects.ACTORS.getEntities();
        for (int i = 0; i < actors.size(); i++) {
            int actorId = actors.get(i);
            if (actorId != blackHoleE.getId()) {
                BlackHoleGravitySystem.components.create(actorId);
            }
        }

        // remove extra game logic:
        IntBag ships = Aspects.SHIPS.getEntities();
        for (int i = 0; i < ships.size(); i++) {
            ShipManager.components.remove(ships.get(i));
        }

        IntBag explos = Aspects.EXPLOSIVES.getEntities();
        for (int i = 0; i < explos.size(); i++) {
            ExplosiveManager.components.remove(explos.get(i));
        }

        IntBag asts = Aspects.ASTEROIDS.getEntities();
        for (int i = 0; i < asts.size(); i++) {
            AsteroidSystem.components.remove(asts.get(i));
        }
    }

    static void savedSolarSystemScene() {
        AiManager.setAiState(true);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dWorldSystem.class).setEnabled(true);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dBodySystem.class).setEnabled(true);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(PlayerInputSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(EntityListenEnterInCameraViewSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(EntityListenExitFromCameraViewSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(SunHeatDmgSystem.class).setEnabled(true);

        SpasholeApp.core.getActiveScene().fadeIn(4000);
        CameraControllerFocusSystem.setTarget(Scenario.LumenGlobals.MAIN_PLANET_UUID, 0);

        SchedulingSystem.schedule(6000, () -> {
            SpasholeApp.ui.getFinalPanel().refresh(SpasholeAssets.textResources.get(RASA_LUMENOV_BYLA_SPASENA_Y_AVNO_ILI_NEYAVNO_ZNAETE_I));
            SpasholeApp.ui.getFinalPanel().show();
            SchedulingSystem.schedule(30000, () -> {
                SpasholeApp.ui.getFinalPanel().hide();
            });

            try {
                scheduleNextEntityForFocus();
            } catch (Throwable t) {
                SpasholeApp.ui.getFinalPanel().refresh(SpasholeAssets.textResources.get(NO_VSELENNAYA_NE_BYLA_IDEAL_NOI));
            }
        });
    }

    private static void scheduleNextEntityForFocus() {
        SchedulingSystem.schedule(5000, () -> {
            IntBag entities = Aspects.POSITIONS.getEntities();
            int entId = entities.get(SpasholeUtils.RANDOM.nextInt(entities.size()));
            CameraControllerFocusSystem.setTarget(
                    SpasholeApp.ARTEMIS_UUIDS.getUuid(
                            SpasholeApp.ARTEMIS_ENGINE.getEntity(entId)
                    ), 0);
            scheduleNextEntityForFocus();
        });
    }

    public static void prepareForFinalScene() {
        SpasholeApp.ARTEMIS_ENGINE.getSystem(AiDynamicSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(AiStaticSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dWorldSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dBodySystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(PlayerInputSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(EntityListenEnterInCameraViewSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(EntityListenExitFromCameraViewSystem.class).setEnabled(false);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(SunHeatDmgSystem.class).setEnabled(false);
        PlayerInfoUpdateSystem.panelRefresher.stop = true;
        SpasholeApp.ui.hide();
    }
}
