package conversion7.spashole.game.overlap.subloaders;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import conversion7.spashole.game.artemis_odb.systems.AnimalManager;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;

import java.util.HashMap;

public class AnimalLoader {
    public static Entity load(float worldX, float worldY, HashMap<String, String> vars, Stage stage) {
        // vars parsing
        RaceComp.Race race;
        String type = vars.get("type");
        if ("aggr".equals(type)) {
            race = RaceComp.Race.ANIMAL_AGGR;
        } else if ("neutral".equals(type)) {
            race = RaceComp.Race.ANIMAL_NEUT;
        } else {
            throw new GdxRuntimeException("Unknown spawn type: " + type);
        }

        return AnimalManager.create(worldX, worldY, race, stage);
    }
}
