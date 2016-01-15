package conversion7.spashole.game.overlap.subloaders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import conversion7.gdxg.core.dialog.AbstractDialog;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.DebugFlags;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.EntityDialogManager;
import conversion7.spashole.game.artemis_odb.systems.EntityEntersCameraViewTriggerComponent;
import conversion7.spashole.game.artemis_odb.systems.EntityEntersCameraViewTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.RaceComp;
import conversion7.spashole.game.artemis_odb.systems.RaceManager;
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetManager;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;
import conversion7.spashole.game.overlap.SceneLoader;
import conversion7.spashole.game.overlap.model.WorldData;
import conversion7.spashole.game.story.Scenario;
import conversion7.spashole.game.story.dialogs.planet.LumenCommonPlanetCallDialog;
import conversion7.spashole.game.story.dialogs.planet.LumenMainPlanetCallDialog;
import conversion7.spashole.game.story.dialogs.planet.LumenMainPlanetFirstDialog;
import org.slf4j.Logger;

import java.util.UUID;

public class PlanetLoader {
    private static final Logger LOG = Utils.getLoggerForClass();
    private static int lumenPanets;
    private static final String LUMEN_PLANET_MAIN_STORY_DIALOG = "LUMEN_PLANET_MAIN_STORY_DIALOG";


    public static int load(WorldData wd, Stage stage) {
        String raceCode = wd.vars.get("race");
        TextureRegion textureRegion;
        RaceComp.Race race;
        if (raceCode.equals("L")) {
            race = RaceComp.Race.LUMEN;
            textureRegion = SpasholeAssets.planet1;
        } else if (raceCode.equals("G")) {
            race = RaceComp.Race.GROK;
            textureRegion = SpasholeAssets.planet1;
        } else if (raceCode.equals("A_A")) {
            race = RaceComp.Race.ANIMAL_AGGR;
            textureRegion = SpasholeAssets.planet1;
        } else if (raceCode.equals("A_N")) {
            race = RaceComp.Race.ANIMAL_NEUT;
            textureRegion = SpasholeAssets.planet1;
        } else {
            throw new GdxRuntimeException("");
        }

        if (race == RaceComp.Race.LUMEN) {
            if (!DebugFlags.CREATE_LUMEN_PLANETS) {
                LOG.info("DebugFlags.CREATE_LUMEN_PLANETS OFF");
                return SceneLoader.NO_ENTITY_CREATED;
            }
            if (!DebugFlags.CREATE_ALL_LUMEN_PLANETS && lumenPanets == 1) {
                LOG.info("DebugFlags.CREATE_ALL_LUMEN_PLANETS OFF");
                return SceneLoader.NO_ENTITY_CREATED;
            }
            lumenPanets++;
        }


        String name = wd.vars.get("name");
        int planetE = PlanetManager.create(name, wd.worldX, wd.worldY, stage, textureRegion, race).getId();
        UUID planetUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(SpasholeApp.ARTEMIS_ENGINE.getEntity(planetE));

        if (name.equals(Scenario.LumenGlobals.MAIN_PLANET_NAME)) {
            if (!DebugFlags.LUMEN_MAIN_PLANET_NOT_CAPTURED) {
                SchedulingSystem.schedule(2000, () -> {
                    PlanetManager.capture(SpasholeApp.ARTEMIS_ENGINE.getEntity(planetE)
                            , RaceComp.Race.GROK);
                });
            }
            PlanetManager.planetSpeakerComponents.create(planetE)
                    .name = Scenario.LumenGlobals.MAIN_PLANET_SPEAKER_1;
            Scenario.LumenGlobals.MAIN_PLANET_UUID = planetUuid;
            EntityEntersCameraViewTriggerComponent cameraViewTriggerComponent =
                    EntityEntersCameraViewTriggerManager.componentCreate(planetE);
            cameraViewTriggerComponent.addRunnable(false, LUMEN_PLANET_MAIN_STORY_DIALOG, () -> {
                if (RaceManager.components.get(planetE).race == RaceComp.Race.LUMEN) {
                    // flag to remove this trigger
                    cameraViewTriggerComponent.runnables.get(LUMEN_PLANET_MAIN_STORY_DIALOG).singleShot = true;
                    AbstractDialog.start(new LumenMainPlanetFirstDialog(SpasholeApp.ui.getDialogWindow(), planetUuid));
                }
            });
        } else if (name.equals(Scenario.GrokGlobals.MAIN_PLANET_NAME)) {
            PlanetManager.planetSpeakerComponents.create(planetE)
                    .name = Scenario.GrokGlobals.MAIN_PLANET_SPEAKER;
            Scenario.GrokGlobals.MAIN_PLANET_UUID = planetUuid;
        }

        if (race == RaceComp.Race.LUMEN) {
            PlanetManager.createStaticGuns(planetE, stage);
        }

        // planet dialogs
        EntityDialogManager.components.create(planetE).dialogBuilder = () -> {
            RaceComp raceComp = RaceManager.components.get(planetE);
            if (raceComp.race == RaceComp.Race.LUMEN) {
                if (SpasholeApp.ARTEMIS_UUIDS.getUuid(SpasholeApp.ARTEMIS_ENGINE.getEntity(planetE))
                        == Scenario.LumenGlobals.MAIN_PLANET_UUID) {
                    return new LumenMainPlanetCallDialog(SpasholeApp.ui.getDialogWindow(), planetUuid);
                } else {
                    return new LumenCommonPlanetCallDialog(SpasholeApp.ui.getDialogWindow(), planetUuid);
                }
            }

            // any for Groks?
            return null;
        };

        return planetE;
    }
}
