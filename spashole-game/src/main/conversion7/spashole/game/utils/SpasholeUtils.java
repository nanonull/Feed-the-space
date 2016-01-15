package conversion7.spashole.game.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;

public class SpasholeUtils extends Utils {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static float worldToScene(float worldValue) {
        return worldValue / SpasholeApp.WORLD_SCALE;
    }

    public static float sceneToWorld(float sceneValue) {
        return SpasholeApp.WORLD_SCALE * sceneValue;
    }

    public static String coordsToString(float x, float y) {
        return String.format ("%.2f:%.2f", x, y);
    }
}
