package conversion7.spashole.game.story
import aurelienribon.tweenengine.Tween
import com.artemis.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import conversion7.gdxg.core.tween.ActorAccessor
import conversion7.spashole.game.DebugFlags
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.artemis_odb.Aspects
import conversion7.spashole.game.artemis_odb.Tags
import conversion7.spashole.game.artemis_odb.systems.*
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetManager
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem
import conversion7.spashole.game.audio.AudioPlayer
import groovy.transform.CompileStatic

@CompileStatic
public class Scenario {

    public static UUID qBrokenShip;
    public static boolean warpEngineInstalled = false

    static def warpEngineInstall() {
        Scenario.warpEngineInstalled = true
//        Journal.addToJournal(SpasholeAssets.textResources.get(new ResKey("RABOCHII_GIPER_DVIGATEL_USTANOVLEN_TEPER_YA_MOGU_U")))
    }

    static
    final ResKey VY_SOVERSHAETE_PRYZHOK_V_GIPER_PROSTRANSTVO_OCHEN_SK = new ResKey('GET_WARP_ENGINE_DIALOG_VY_SOVERSHAETE_PRYZHOK_V_GIPER_PROSTRANSTVO_OCHEN_SK')
    public static final ResKey PRODOLZHIT = new ResKey("PRODOLZHIT");

    static def goHomeState(SpasholeDialog dialog) {
        dialog.text(SpasholeAssets.textResources.get(VY_SOVERSHAETE_PRYZHOK_V_GIPER_PROSTRANSTVO_OCHEN_SK))
        dialog.option(SpasholeAssets.textResources.get(PRODOLZHIT), {
            dialog.complete()
            SchedulingSystem.scheduleOnNextStep({
                Scenario.jumpToWarpScene()
            })
        })
    }

    static def jumpToWarpScene() {
        FinalScene.prepareForFinalScene()

        Entity playerE = SpasholeApp.ARTEMIS_TAGS.getEntity(Tags.PLAYER_ENTITY)
        def shipActor = Actor2dManager.components.get(playerE)
        def shipPos = Position2dSystem.components.get(playerE);
        CameraControllerFocusSystem.cameraController.camera.position.set(
                shipPos.pos.x, shipPos.pos.y, 0)

        // artemis entity
        Entity effectE = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        def effectUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(effectE)
        CameraControllerFocusSystem.setTarget(effectUuid, 0)
        def effectPos = Position2dSystem.components.create(effectE);
        effectPos.pos.set(shipPos.pos)
        Rotation2dSystem.components.create(effectE);
        NameManager.components.create(effectE).name = "WarpSceneEffect";

        // visual
        def stage = shipActor.actor.getStage()
        Group group = new Group();
        stage.addActor(group);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(effectE);
        actor2dComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeApp.WORLD_SCALE);

        Image warpImage = new Image(SpasholeAssets.warpImg);
        group.addActor(warpImage);
        warpImage.setOrigin(Align.center);
        Actor2dManager.adjustPositionCenterBySize(warpImage);

        // animations
        float totalSec = 3f
        int totalMs = (totalSec * 1000).toInteger()

        Tween.to(warpImage, ActorAccessor.ROTATION, (totalSec + 0.5f).toFloat())
                .target(4242)
                .start(SpasholeApp.tweenManager)

        float startScale = 1.5f
        float endScale = 1.8f
        warpImage.setScale(startScale);
        Tween.to(warpImage, ActorAccessor.SCALE_XY, totalSec)
                .target(endScale, endScale)
                .start(SpasholeApp.tweenManager)

        warpImage.setColor(1, 1, 1, 0)
        float alphaFadeIn = (totalSec / 3f).toFloat()
        Tween.to(warpImage, ActorAccessor.COLOR_ALPHA, alphaFadeIn)
                .target(1f)
                .start(SpasholeApp.tweenManager)

        int fadeOutStartMs = (totalMs / 2).toInteger()
        int fadeOutDurMs = (totalSec * 1000 - fadeOutStartMs).toInteger()
        SchedulingSystem.schedule(fadeOutStartMs, {
            SpasholeApp.core.activeScene.fadeOut(Color.BLACK, fadeOutDurMs)
        })

        float initShipScale = shipActor.actor.getScaleX()
        float endShipScale = (initShipScale * 5f).toFloat()
        Tween.to(shipActor.actor, ActorAccessor.SCALE_XY, alphaFadeIn)
                .target(endShipScale, endShipScale)
                .start(SpasholeApp.tweenManager)

        AudioPlayer.stopAll()
        AudioPlayer.play(SpasholeAssets.warpAudio);

        SchedulingSystem.schedule(totalMs + 50, {
            DestroyEntitySystem.components.create(playerE)
            DestroyEntitySystem.components.create(effectE)
            FinalScene.start(stage)
        })

    }

    static class GrokGlobals {
        public static
        final String MAIN_PLANET_NAME = SpasholeAssets.textResources.get(new ResKey("GROK_GLOBALS_MAIN_PLANET_NAME"))
        public static UUID MAIN_PLANET_UUID

        private static Integer MAIN_PLANET_POS

        static boolean isRaceAlive() {
            if (!DebugFlags.GROK_ALIVE_BY_SCENARIO) {
                return false
            } else {
                def planets = Aspects.PLANETS.getEntities()
                for (int i = 0; i < planets.size(); i++) {
                    def raceComp = RaceManager.components.getSafe(planets.get(i))
                    if (raceComp?.race == RaceComp.Race.GROK){
                        return true
                    }
                }

                def ships = Aspects.SHIPS.getEntities()
                for (int i = 0; i < ships.size(); i++) {
                    def raceComp = RaceManager.components.getSafe(ships.get(i))
                    if (raceComp?.race == RaceComp.Race.GROK){
                        return true
                    }
                }

                return false
            }
        }

        static String getMAIN_PLANET_POS() {
            if (MAIN_PLANET_POS == null) {
                def planetComponent = PlanetManager.planetComponents.get(
                        SpasholeApp.ARTEMIS_UUIDS.getEntity(MAIN_PLANET_UUID))
                MAIN_PLANET_POS = planetComponent.positionInSolarSystem;
            }
            return MAIN_PLANET_POS
        }
        public static String MAIN_PLANET_SPEAKER = SpasholeAssets.textResources.get(new ResKey("GROK_MAIN_SPEAKER"))
        static boolean playerKnowsAboutGroks = false
        static boolean playerSawGrokShip = false

        static def playerKnowsAboutGroks() {
            playerKnowsAboutGroks = true
        }

        static def playerSawGrokShip() {
            playerSawGrokShip = true
        }
    }


    static class LumenGlobals {
        static boolean firstContactCancelled = false
        static int playerKnowsAboutLumenPhys
        public static
        final ResKey ETO_SVYAZANO_S_NASHEI_RELIGIEI_I_BIOLOGIEI = new ResKey("ETO_SVYAZANO_S_NASHEI_RELIGIEI_I_BIOLOGIEI");
        static
        final String ANSWER_ON_WHY_HAVE_SUCH_BIO = SpasholeAssets.textResources.get(ETO_SVYAZANO_S_NASHEI_RELIGIEI_I_BIOLOGIEI)


        static boolean contactAcceptedWithLumenPlanet
        static boolean talkedAboutLumenProblem
        static boolean acceptedQuestHelpWithGroks
        public static UUID MAIN_PLANET_UUID

        private static String LUMEN_MAIN_PLANET_POS

        public static String getLUMEN_MAIN_PLANET_POS() {
            if (LUMEN_MAIN_PLANET_POS == null) {
                def planetComponent = PlanetManager.planetComponents.get(
                        SpasholeApp.ARTEMIS_UUIDS.getEntity(MAIN_PLANET_UUID))
                LUMEN_MAIN_PLANET_POS = planetComponent.positionInSolarSystem;
            }
            return LUMEN_MAIN_PLANET_POS
        }
        public static
        final String MAIN_PLANET_NAME = SpasholeAssets.textResources.get(new ResKey("LUMEN_MAIN_PLANET_NAME"))
        static String GROK_DESC = SpasholeAssets.textResources.get(new ResKey("ONI_PRIBYLI_SUDA10_MEGASEKUND_NAZAD_OTKU"))

        static boolean playerToldLumensBreakHisEngine = false
        static boolean playerKnowsLumensCatchHimInHyper = false
        public static String MAIN_PLANET_SPEAKER_1 = SpasholeAssets.textResources.get(new ResKey("LUM_MAIN_SPEAKER_1"))
        public static String MAIN_PLANET_SPEAKER_2 = SpasholeAssets.textResources.get(new ResKey("LUM_MAIN_SPEAKER_2"))

        static def noteLumenPhys() {
            if (!playerKnowsAboutLumenPhys) {
                Journal.addToJournal SpasholeAssets.textResources.get(new ResKey("LUMENY_FIZIOLOGICHESKI_NE_MOGUT_NANESTI_VR"))
            }
            playerKnowsAboutLumenPhys++
        }

        static def noteLumensHasProblemWithGroks() {
            talkedAboutLumenProblem = true
            Journal.addToJournal(SpasholeAssets.textResources.get(new ResKey("RASA_GROKOV_NAPALA_NA_LUMENOV")))
        }

        static def acceptQuestHelpWithGroks() {
            acceptedQuestHelpWithGroks = true
            Journal.addToJournal(
                    SpasholeAssets.textResources.get(new ResKey("YA_SOGLASILSYA_POMOCH_LUMENAM_V_VOINE_PRO"))
                            + GrokGlobals.MAIN_PLANET_POS)
            Scenario.GrokGlobals.playerKnowsAboutGroks()
        }

        static def noteLumensCatchMeInHyper() {
            playerKnowsLumensCatchHimInHyper = true
            Journal.addToJournal(SpasholeAssets.textResources.get(new ResKey("MENYA_PEREHVATILI_V_GIPER_PROSTRANSTVE_PRE")))
        }

        static def noteGroksCouldHaveHyperEngine() {
            Journal.addToJournal(SpasholeAssets.textResources.get(new ResKey("LUMENY_GOVORYAT_CHTO_GIPER_DVIGATEL_MOZH")))
        }
    }

}
