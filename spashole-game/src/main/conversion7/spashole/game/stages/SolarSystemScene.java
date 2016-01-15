package conversion7.spashole.game.stages;

import aurelienribon.tweenengine.Tween;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.game_scene.GameScene2d;
import conversion7.gdxg.core.dialog.AbstractDialog;
import conversion7.gdxg.core.tween.ActorAccessor;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.Tags;
import conversion7.spashole.game.artemis_odb.systems.AsteroidSystem;
import conversion7.spashole.game.artemis_odb.systems.CameraControllerFocusSystem;
import conversion7.spashole.game.artemis_odb.systems.EntityInventoryComp;
import conversion7.spashole.game.artemis_odb.systems.EntityInventoryManager;
import conversion7.spashole.game.artemis_odb.systems.HealthComponent;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.HumanManager;
import conversion7.spashole.game.artemis_odb.systems.InventoryItemComp;
import conversion7.spashole.game.artemis_odb.systems.InventoryItemManager;
import conversion7.spashole.game.artemis_odb.systems.NameComponent;
import conversion7.spashole.game.artemis_odb.systems.NameManager;
import conversion7.spashole.game.artemis_odb.systems.PlayerInputSystem;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.ShipManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.solar.ParallaxSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.SolarSystem;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;
import conversion7.spashole.game.overlap.SceneLoader;
import conversion7.spashole.game.story.FinalScene;
import conversion7.spashole.game.story.dialogs.GetWarpEngineDialog;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.slf4j.Logger;

import java.util.UUID;

public class SolarSystemScene extends GameScene2d {

    private static final Logger LOG = Utils.getLoggerForClass();

    public static final float PERSON_RADIUS = SpasholeUtils.sceneToWorld(7f);
    public static final float PERSON_SENSOR_RADIUS = PERSON_RADIUS + SpasholeUtils.sceneToWorld(7f);
    public static final float HP_MLT = 1;
    public static final int SHIP_GUN_DMG = (int) (3 * HP_MLT);
    public static final int ANIMAL_DMG = (int) (2 * HP_MLT);
    public static final int HUMAN_DMG = (int) (1 * HP_MLT);

    public static final int ASTERIOD_HP_PER_LVL = (int) (6 * HP_MLT);
    public static final int ANIMAL_HP = (int) (10 * HP_MLT);
    public static final int HUMAN_HP = (int) (5 * HP_MLT);
    public static final int PLANET_BASE_HEALTH = (int) (30 * HP_MLT);
    public static final int SHIP_HP = (int) (20 * HP_MLT);
    public static final int PLAYER_SHIP_HP = (int) (50 * HP_MLT);
    public static final int STAT_GUN_HP = (int) (15 * HP_MLT);
    public static final int INV_ITEM_HP = (int) (10 * HP_MLT);

    private Stage stageEffects;
    Image fadeImage;
    private static final int PLAYER_DEATH_FADE_OUT_DUR_MS = 2000;

    public static int getPlayerEntity() {
        Entity entity = SpasholeApp.ARTEMIS_TAGS.getEntity(Tags.PLAYER_ENTITY);
        if (entity == null) {
            return -1;
        } else {
            return entity.getId();
        }
    }

    @Override
    public void init() {
        super.init();
        Stage sceneStage = getStage2d();
        initEffectsStage();

        ParallaxSystem.init(sceneStage, null, SpasholeApp.graphic.getCameraController());
        SceneLoader.loadOverlapScene(sceneStage, "scene1\\scenes\\MainScene.dt");
//        createRuntimeScene();
        UUID playerUuid = HumanManager.createHumanPerson(0, 0, sceneStage);
        Entity playerShipE = SpasholeApp.ARTEMIS_TAGS.getEntity(Tags.PLAYER_SHIP);
        ShipManager.setPilot(playerUuid, playerShipE);
        RaceManager.components.create(playerShipE).race = RaceComp.Race.HUMAN;
        Entity playerHumanE = SpasholeApp.ARTEMIS_UUIDS.getEntity(playerUuid);
        RaceManager.components.create(playerHumanE).race = RaceComp.Race.HUMAN;
        NameComponent shipName = NameManager.components.get(playerShipE);
        shipName.name = "player-" + shipName.name;
        HealthComponent shipHP = HealthSystem.components.get(playerShipE);
        shipHP.setup(DebugFlags.PLAYER_SHIP_NORM_HP ? SolarSystemScene.PLAYER_SHIP_HP
                : 9999);

        Runnable endGameRunnable = () -> {
            if (!FinalScene.started) {
                FinalScene.prepareForFinalScene();
                fadeOut(Color.BLACK, PLAYER_DEATH_FADE_OUT_DUR_MS);
                SchedulingSystem.schedule(PLAYER_DEATH_FADE_OUT_DUR_MS, () -> {
                    FinalScene.start(sceneStage);
                });
            }
        };
        shipHP.onDeathTrigger = endGameRunnable;
        HealthComponent humanHP = HealthSystem.components.get(playerHumanE);
        humanHP.onDeathTrigger = endGameRunnable;

        Vector2 shipBodyPos = Box2dBodySystem.components.get(playerShipE).body.getPosition();
        CameraControllerFocusSystem.getCameraController().getCamera()
                .position.set(shipBodyPos.x, shipBodyPos.y, 0);
        EntityInventoryComp shipInventoryComp = EntityInventoryManager.components.create(playerShipE);
        shipInventoryComp.addItemTrigger(InventoryItemComp.Type.WARP_ENGINE, () -> {
            AbstractDialog.start(new GetWarpEngineDialog(playerShipE.getId(), SpasholeApp.ui.getDialogWindow()));
        });
        if (!DebugFlags.DONT_CREATE_INVENT_ITEM_NEAR_PLAYER) {
            InventoryItemManager.create(InventoryItemComp.Type.WARP_ENGINE
                    , shipBodyPos.x - SpasholeUtils.sceneToWorld(100), shipBodyPos.y, sceneStage);
            InventoryItemManager.create(InventoryItemComp.Type.WARP_ENGINE
                    , shipBodyPos.x - SpasholeUtils.sceneToWorld(150), shipBodyPos.y, sceneStage);
        }

        SolarSystem.postInit();

        if (!DebugFlags.DEBUG_MODE_INACTIVE) {
            addDebugPoint(0, 0);
            addDebugPoint(1, 0);
            addDebugPoint(10, 0);
            addDebugPoint(20, 0);
        }
    }

    private void initEffectsStage() {
        Stage sceneStage = getStage2d();
        Viewport viewport = SpasholeApp.ui.screenViewport;
        stageEffects = new Stage(viewport, sceneStage.getBatch());

        Group group = stageEffects.getRoot();

        fadeImage = new Image(CommonAssets.pixelWhite);
        group.addActor(fadeImage);
        fadeImage.setOrigin(Align.center);
        fadeImage.setSize(viewport.getScreenWidth(), viewport.getScreenHeight());
        fadeImage.setColor(0, 0, 0, 0);
    }

    @Override
    public void fadeOut(Color toColor, int durationMs) {
        fadeImage.setColor(toColor.r, toColor.g, toColor.b, 0);
        Tween.to(fadeImage, ActorAccessor.COLOR_ALPHA, durationMs / 1000f)
                .target(1f)
                .start(SpasholeApp.tweenManager);
    }

    @Override
    public void fadeIn(int durationMs) {
        Tween.to(fadeImage, ActorAccessor.COLOR_ALPHA, durationMs / 1000f)
                .target(0)
                .start(SpasholeApp.tweenManager);
    }

    @Override
    public void draw() {
        super.draw();
        stageEffects.draw();
    }

    private void createRuntimeScene() {
        UUID playerUuid = HumanManager.createHumanPerson(
                SpasholeUtils.sceneToWorld(150),
                SpasholeUtils.sceneToWorld(150), getStage2d());
        Entity playerPerson = SpasholeApp.ARTEMIS_UUIDS.getEntity(playerUuid);
        Entity ship = ShipManager.createShip(
                SpasholeUtils.sceneToWorld(200)
                , SpasholeUtils.sceneToWorld(200)
                , getStage2d());

        PlayerInputSystem.setTarget(playerPerson.getId());

        int asteroidOffset = (int) (SpasholeUtils.sceneToWorld(200));
        AsteroidSystem.createAsteroid(asteroidOffset, 0, 3, getStage2d());
        AsteroidSystem.createAsteroid(-asteroidOffset, -asteroidOffset, 3, getStage2d());
        AsteroidSystem.createAsteroid(asteroidOffset, -asteroidOffset, 3, getStage2d());
        AsteroidSystem.createAsteroidWithGuns(-asteroidOffset, asteroidOffset, getStage2d());
    }

    private void addDebugPoint(float x, float y) {
        Image image = new Image(CommonAssets.pixelWhite);
        getStage2d().addActor(image);
        image.setScale(0.5f);
        image.setZIndex(Integer.MAX_VALUE);
        image.setPosition(x, y);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }
}
