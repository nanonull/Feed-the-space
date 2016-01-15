package conversion7.spashole.game;


import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.InvocationStrategy;
import com.artemis.SystemInvocationStrategy;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import conversion7.gdxg.core.AbstractClientCore;
import conversion7.gdxg.core.CommonApp;
import conversion7.gdxg.core.CommonConstants;
import conversion7.gdxg.core.custom2d.ui_logger.UiLogger;
import conversion7.gdxg.core.game_scene.GameScene;
import conversion7.gdxg.core.game_scene.GameScene2d;
import conversion7.gdxg.core.game_scene.camera.CameraController;
import conversion7.gdxg.core.game_scene.camera.CameraController2D;
import conversion7.gdxg.core.dialog.AbstractDialog;
import conversion7.gdxg.core.utils.Utils;
import conversion7.gdxg.core.utils.props.CommonPropertyKey;
import conversion7.spashole.game.artemis_odb.Aspects;
import conversion7.spashole.game.artemis_odb.CommonMappers;
import conversion7.spashole.game.artemis_odb.Tags;
import conversion7.spashole.game.artemis_odb.systems.ActivationTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.Actor2dColorSystem;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.AnimalManager;
import conversion7.spashole.game.artemis_odb.systems.AsteroidSystem;
import conversion7.spashole.game.artemis_odb.systems.BulletManager;
import conversion7.spashole.game.artemis_odb.systems.CameraControllerFocusSystem;
import conversion7.spashole.game.artemis_odb.systems.DestroyEntitySystem;
import conversion7.spashole.game.artemis_odb.systems.DisableEntitySystem;
import conversion7.spashole.game.artemis_odb.systems.EnableEntitySystem;
import conversion7.spashole.game.artemis_odb.systems.EntityDialogManager;
import conversion7.spashole.game.artemis_odb.systems.EntityEntersCameraViewTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.EntityExitsCameraViewTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.EntityInventoryManager;
import conversion7.spashole.game.artemis_odb.systems.EntityListenEnterInCameraViewSystem;
import conversion7.spashole.game.artemis_odb.systems.EntityListenExitFromCameraViewSystem;
import conversion7.spashole.game.artemis_odb.systems.ExplosiveManager;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.HumanManager;
import conversion7.spashole.game.artemis_odb.systems.InventoryItemManager;
import conversion7.spashole.game.artemis_odb.systems.LoggerSystem;
import conversion7.spashole.game.artemis_odb.systems.MovementConstantManager;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.PlayerInfoUpdateSystem;
import conversion7.spashole.game.artemis_odb.systems.PlayerInputSystem;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.Rotation2dSystem;
import conversion7.spashole.game.artemis_odb.systems.ShipManager;
import conversion7.spashole.game.artemis_odb.systems.TextureAnimation2dSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicMoveToEntityManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.AiGrokManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiManager;
import conversion7.spashole.game.artemis_odb.systems.ai.AiStaticSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dAdditionalBodiesManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dMotorJointActionSystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dMotorJointManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dWorldSystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.artemis_odb.systems.gun.StationaryGunSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.BlackHoleGravitySystem;
import conversion7.spashole.game.artemis_odb.systems.solar.ParallaxSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.SolarSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.SunHeatDmgSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.SunManager;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.LumenPlanetSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetAnimalSpawnSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetBaseSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetManager;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetShipSpawnSystem;
import conversion7.spashole.game.artemis_odb.systems.time.IntervalExecutorSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;
import conversion7.spashole.game.audio.AudioPlayer;
import conversion7.spashole.game.stages.SolarSystemScene;
import conversion7.spashole.game.story.dialogs.IntroDialog;
import conversion7.spashole.game.ui.CentralPanel;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.network.EntityTrackerServer;
import org.slf4j.Logger;

import java.util.concurrent.Callable;

public class ClientCore extends AbstractClientCore {

    private static final Logger LOG = Utils.getLoggerForClass();

    public SolarSystemScene solarSystemScene;
    private boolean worldPause = false;

    @Override
    protected WorldConfigurationBuilder getArtemisOdbConfigBuilder() {
        return super.getArtemisOdbConfigBuilder().with(
                // MANAGERS:
                new Actor2dManager()
                , new NameManager()
                , new ExplosiveManager()
                , new BulletManager()
                , new EntityTracker(registerArtemisOdbEntityTracker())
                , new Box2dSensorManager()
                , new ShipManager()
                , new HumanManager()
                , new Box2dAdditionalBodiesManager()
                , new CommonMappers()
                , new AiGrokManager()
                , new AiDynamicMoveToEntityManager()

                , new AnimalManager()
                , new ActivationTriggerManager()
                , new PlanetManager()
                , new EntityEntersCameraViewTriggerManager()
                , new EntityExitsCameraViewTriggerManager()
                , new EntityDialogManager()
                , new RaceManager()
                , new SunManager()
                , new Box2dMotorJointManager()
                , new MovementConstantManager()
                , new InventoryItemManager()
                , new EntityInventoryManager()

                // SYSTEMS:
                , new DisableEntitySystem()
                , new EnableEntitySystem()
                , new SchedulingSystem()
                , new IntervalExecutorSystem()

                , new TextureAnimation2dSystem()
                , new LoggerSystem()
                , new GunSystem()
                // before Box2dWorldSystem
                , new StationaryGunSystem()
                , new Box2dMotorJointActionSystem()
                , new Box2dWorldSystem()
                , new PlayerInputSystem()

                , new HealthSystem()
                // AsteroidSystem.breakAsteroid works if this system after Box2dWorldSystem
                , new AsteroidSystem()
                , new Box2dBodySystem()
                , new Position2dSystem()
                , new Rotation2dSystem()
                , new Actor2dColorSystem()
                , new CameraControllerFocusSystem()
                , new EntityListenEnterInCameraViewSystem()
                , new EntityListenExitFromCameraViewSystem()
                , new PlanetBaseSystem()

                , new AiStaticSystem()
                , new AiDynamicSystem()
                , new SolarSystem()
                , new LumenPlanetSystem()
                , new PlanetShipSpawnSystem()
                , new PlanetAnimalSpawnSystem()
                , new SunHeatDmgSystem()
                , new ParallaxSystem()
                , new BlackHoleGravitySystem()

                , new DestroyEntitySystem()
        );
    }

    @Override
    protected SystemInvocationStrategy getCustomArtemisStrategy() {
        return new InvocationStrategy() {
            @Override
            protected void process(Bag<BaseSystem> systems) {
                if (worldPause) {
                    Object[] systemsData = systems.getData();
                    for (int i = 0, s = systems.size(); s > i; i++) {
                        Object system = systemsData[i];
                        if (system instanceof CameraControllerFocusSystem) {
                            ((BaseSystem) system).process();
                            updateEntityStates();
                        }
                    }
                } else {
                    super.process(systems);
                }
            }
        };
    }

    @Deprecated
    public Stage getActiveStage2d() {
        return ((GameScene2d) SpasholeApp.core.getActiveScene()).getStage2d();
    }

    public boolean isWorldPause() {
        return worldPause;
    }

    public void setWorldPause(boolean worldPause) {
        this.worldPause = worldPause;
    }

    @Override
    public void create() {
        super.create();

        DebugFlags.init();
        setCrashOnErrorInRender(true);
        SpasholeApp.core = this;
        SpasholeAssets.loadAll();

        registerTweenEngine();
        registerArtemisOdbEngine();
        Aspects.init();

        OrthographicCamera camera = new OrthographicCamera(
                CommonConstants.SCREEN_WIDTH_IN_PX, CommonConstants.SCREEN_HEIGHT_IN_PX);

        CameraController cameraController = new CameraController2D(camera,
                CommonApp.commonProperties.getInteger(
                        CommonPropertyKey.AbstractClientGraphic_CameraMovementSpeed));

        graphic = new ClientGraphic(
                SpasholeApp.WORLD_SCALE,
                CommonConstants.SCREEN_WIDTH_IN_PX,
                CommonConstants.SCREEN_HEIGHT_IN_PX,
                cameraController);

        LOG.info("Created.");
        initialized = true;

        startGame();
    }

    private EntityTrackerServer registerArtemisOdbEntityTracker() {
        EntityTrackerServer entityTrackerServer = new EntityTrackerServer();
        entityTrackerServer.start();
        return entityTrackerServer;
    }

    @Override
    public void render() {
        super.render();
        if (!DebugFlags.BOX2D_DEBUG_RENDER_DISABLED) {
            SpasholeApp.box2dWorldDebugRenderer.render(SpasholeApp.box2dWorld,
                    SpasholeApp.graphic.getCameraController().getCamera().combined);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            setEcsEngineEnabled(!ecsEngineEnabled);
            UiLogger.addInfoLabel("ecsEngineEnabled: " + ecsEngineEnabled);
        }
    }

    @Override
    public void dispose() {
        AudioPlayer.stopAll();
    }

    private void startGame() {
        LOG.info("startGame");
        int logEntity = CommonApp.ARTEMIS_ENGINE.create();
        LoggerSystem.components.create(logEntity).callable = new Callable<String>() {
            int tick = 0;

            @Override
            public String call() throws Exception {
                tick++;
                if (tick > 10) {
                    DestroyEntitySystem.components.create(logEntity);
                }
                return "tick " + tick;
            }
        };

        solarSystemScene = GameScene.build(SolarSystemScene.class);
        solarSystemScene.getStage2d().getViewport().setCamera(graphic.getCameraController().getCamera());
        Entity playerE = SpasholeApp.ARTEMIS_TAGS.getEntity(Tags.PLAYER_ENTITY);
        SchedulingSystem.scheduleOnNextStep(() -> {
            AbstractDialog.start(new IntroDialog(playerE, SpasholeApp.ui.getDialogWindow()));
        });

        activateScene(solarSystemScene);

        PlayerInfoUpdateSystem.scheduleUpdate();

        AiManager.setAiState(DebugFlags.AI_ENABLED);
        if (!DebugFlags.ALL_SYSTEM_ACTIVE) {
            LOG.info("DebugFlags.ALL_SYSTEM_ACTIVE OFF");
            SpasholeApp.ARTEMIS_ENGINE.getSystem(SolarSystem.class).setEnabled(true);
            SpasholeApp.ARTEMIS_ENGINE.getSystem(Box2dMotorJointActionSystem.class).setEnabled(true);
        }
        CameraControllerFocusSystem.getCameraController().setEnabled(DebugFlags.CAMERA_CNTRL_ENABLED);

        SchedulingSystem.schedule(2000, () -> {
            CentralPanel centralPanel = SpasholeApp.ui.getCentralPanel();
            centralPanel.refresh("Move: WSAD\n" +
                    "Fire: K\n" +
                    "Exit ship: E");
            centralPanel.show();
            SchedulingSystem.schedule(8000, () -> {
                centralPanel.hide();
            });
        });
    }
}
