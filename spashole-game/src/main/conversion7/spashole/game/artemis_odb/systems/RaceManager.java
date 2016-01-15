package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class RaceManager extends Manager {
    public static ComponentMapper<RaceComp> components;

    private static ObjectMap<RaceComp.Race, ObjectSet<RaceComp.Race>> enemies = new ObjectMap<>();
    private static ObjectMap<RaceComp.Race, ObjectSet<RaceComp.Race>> allies = new ObjectMap<>();

    static {
        // init
        for (RaceComp.Race race : RaceComp.Race.values()) {
            enemies.put(race, new ObjectSet<>());
            allies.put(race, new ObjectSet<>());
        }

        addRelation(RaceComp.Race.HUMAN, RaceComp.Race.LUMEN, allies);

        addRelation(RaceComp.Race.GROK, RaceComp.Race.LUMEN, enemies);
        addRelation(RaceComp.Race.GROK, RaceComp.Race.HUMAN, enemies);

        for (RaceComp.Race race : RaceComp.Race.values()) {
            addRelation(RaceComp.Race.ANIMAL_AGGR, race, enemies);
        }
    }

    private static void addRelation(RaceComp.Race race1, RaceComp.Race race2
            , ObjectMap<RaceComp.Race, ObjectSet<RaceComp.Race>> relationList) {
        ObjectSet<RaceComp.Race> races1 = relationList.get(race1);
        races1.add(race2);

        ObjectSet<RaceComp.Race> races2 = relationList.get(race2);
        races2.add(race1);
    }

    public static boolean areEnemies(RaceComp.Race race1, RaceComp.Race race2) {
        return enemies.get(race1).contains(race2);
    }

    public static boolean areAllies(RaceComp.Race race1, RaceComp.Race race2) {
        return allies.get(race1).contains(race2);
    }
}
