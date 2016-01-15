package conversion7.spashole.game.overlap;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.AsteroidSystem;
import conversion7.spashole.game.artemis_odb.systems.ShipManager;
import conversion7.spashole.game.artemis_odb.systems.solar.SolarSystem;
import conversion7.spashole.game.artemis_odb.systems.solar.SunManager;
import conversion7.spashole.game.overlap.model.OverlapSceneData;
import conversion7.spashole.game.overlap.model.WorldData;
import conversion7.spashole.game.overlap.subloaders.AnimalLoader;
import conversion7.spashole.game.overlap.subloaders.PlanetLoader;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SceneLoader {

    public static final String BASE_FOLDER = SpasholeAssets.RES_FOLDER + "overlap/";
    /** Units of overlap scene to pixels on screen */
    public static final float OVERLAP_TO_SCENE = 1f;
    public static final int NO_ENTITY_CREATED = -1;
    private static HashMap<String, String> vars = new HashMap<>();
    static WorldData worldData = new WorldData();

    public static void loadOverlapScene(Stage stage, String scenePath) {
        String sceneJson;
        try {
            sceneJson = FileUtils.readFileToString(new File(BASE_FOLDER + scenePath));
        } catch (IOException e) {
            throw new GdxRuntimeException(e);
        }
        OverlapSceneData overlapSceneData = SpasholeUtils.GSON.fromJson(sceneJson, OverlapSceneData.class);
        for (OverlapSceneData.Composite.SImage gameObject : overlapSceneData.composite.sImages) {
            loadImageObject(gameObject, stage);
        }
        for (OverlapSceneData.Composite.SLabel sLabel : overlapSceneData.composite.sLabels) {
            loadLabelObject(sLabel, stage);
        }

    }

    private static void loadLabelObject(OverlapSceneData.Composite.SLabel obj, Stage stage) {
        worldData.parseSceneObject(obj);

        int createdEntityId = NO_ENTITY_CREATED;
        switch (worldData.objectCode) {
            case "start":
                break;
            case "solar system end":
                SolarSystem.setEnd(worldData.worldX, worldData.worldY);
                break;
            case "asteriod":
                if (vars.containsKey("amount")) {
                    AsteroidSystem.createRandomGroupOfAsteroids(worldData.worldX, worldData.worldY
                            , stage);
                } else {
                    createdEntityId = AsteroidSystem.createAsteroid(
                            worldData.worldX, worldData.worldY
                            , 3, stage).getId();
                }
                break;
            case "ship":
                createdEntityId = ShipManager.createShip(worldData.worldX, worldData.worldY
                        , stage).getId();
                break;
            case "spawn":
                createdEntityId = AnimalLoader.load(worldData.worldX, worldData.worldY
                        , worldData.vars, stage).getId();
                break;
            case "planet":
                createdEntityId = PlanetLoader.load(worldData, stage);
                break;
            case "sun":
                createdEntityId = SunManager.create(worldData.worldX, worldData.worldY, stage);
                SolarSystem.setSunEntity(createdEntityId);
                break;
            default:
                SpasholeUtils.logErrorWithCurrentStacktrace(
                        createGameObjectMsg("Unknown objectCode: " + worldData.objectCode, obj));
        }

        if (createdEntityId > NO_ENTITY_CREATED) {
            SceneLoaderTags.handleTags(obj.tags, createdEntityId);
        }
    }

    private static void loadImageObject(OverlapSceneData.Composite.SImage gameObject, Stage stage) {
        String objectCode = gameObject.imageName;
        loadArgs(gameObject.customVars, vars);
        float sceneX = gameObject.x * OVERLAP_TO_SCENE;
        float worldX = SpasholeUtils.sceneToWorld(sceneX);
        float sceneY = gameObject.y * OVERLAP_TO_SCENE;
        float worldY = SpasholeUtils.sceneToWorld(sceneY);

        int createdEntityId = NO_ENTITY_CREATED;
        switch (objectCode) {
            case "a":
                if (vars.containsKey("amount")) {
                    AsteroidSystem.createRandomGroupOfAsteroids(worldX, worldY, stage);
                } else {
                    createdEntityId = AsteroidSystem.createAsteroid(worldX, worldY, 3, stage).getId();
                }
                break;
            case "s":
                createdEntityId = ShipManager.createShip(worldX, worldY, stage).getId();
                break;
            default:
                SpasholeUtils.logErrorWithCurrentStacktrace(createGameObjectMsg("Unknown objectCode: " + objectCode, gameObject));
        }

        if (createdEntityId > NO_ENTITY_CREATED) {
            SceneLoaderTags.handleTags(gameObject.tags, createdEntityId);
        }
    }

    public static HashMap<String, String> loadArgs(String customVars
            , HashMap<String, String> clearAndLoadTo) {
        clearAndLoadTo.clear();
        if (customVars != null) {
            for (String argPair : customVars.split(";")) {
                String[] arg = argPair.split(":");
                clearAndLoadTo.put(arg[0], arg[1]);
            }
        }
        return clearAndLoadTo;
    }

    private static String createGameObjectMsg(String msgPrefix, OverlapSceneData.Composite.SImage gameObject) {
        return msgPrefix + " on: uniqueId=" + gameObject.uniqueId + ", imageName=" + gameObject.imageName;
    }

    private static String createGameObjectMsg(String msgPrefix, OverlapSceneData.Composite.SLabel gameObject) {
        return msgPrefix + " on: uniqueId=" + gameObject.uniqueId + ", text=" + gameObject.text;
    }

}
