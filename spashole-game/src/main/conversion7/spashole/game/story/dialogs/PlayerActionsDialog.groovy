package conversion7.spashole.game.story.dialogs

import com.artemis.Entity
import com.badlogic.gdx.utils.GdxRuntimeException
import conversion7.gdxg.core.dialog.AbstractDialog
import conversion7.gdxg.core.dialog.QuestOption
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.artemis_odb.systems.*
import conversion7.spashole.game.story.Scenario
import conversion7.spashole.game.story.SpasholeDialog
import groovy.transform.CompileStatic

@CompileStatic
public class PlayerActionsDialog extends SpasholeDialog {

    public static final ResKey SKANIROVAT_BLIZHNII_KOSMOS = new ResKey("SKANIROVAT_BLIZHNII_KOSMOS");
    public static
    final ResKey REZUL_TATY_SKANIROVANIYA_SOLNECHNOI_SISTEM = new ResKey("REZUL_TATY_SKANIROVANIYA_SOLNECHNOI_SISTEM");
    public static final ResKey PROVERIT_SISTEMY = new ResKey("PROVERIT_SISTEMY");
    public static
    final ResKey VY_PROVODITE_BYSTRYI_OSMOTR_SISTEM_NGIPE = new ResKey("VY_PROVODITE_BYSTRYI_OSMOTR_SISTEM_NGIPE");
    public static final ResKey VSE_SISTEMY_V_NORME = new ResKey("VSE_SISTEMY_V_NORME");
    public static final ResKey PROVERIT_GIPER_DVIGATEL = new ResKey("PROVERIT_GIPER_DVIGATEL");
    public static
    final ResKey GIPER_DVIGATEL_RAZRUSHEN_VY_NE_MOZHETE_E = new ResKey("GIPER_DVIGATEL_RAZRUSHEN_VY_NE_MOZHETE_E");
    public static final ResKey VY_V_KORABLE_VASHI_DEISTVIYA = new ResKey("VY_V_KORABLE_VASHI_DEISTVIYA");
    public static final ResKey PLANETS_IN_RADIUS_OF_CONTACT = new ResKey("PLANETS_IN_RADIUS_OF_CONTACT");
    public static final ResKey CALL_PLANET = new ResKey("CALL_PLANET");
    public static
    final ResKey VY_V_OTKRYTOM_KOSMOSE_VASHI_DEISTVIYA = new ResKey("VY_V_OTKRYTOM_KOSMOSE_VASHI_DEISTVIYA");
    public static
    final ResKey VY_PODALI_ZAPROS_NA_SVYAZ_S_PREDSTAVITELE = new ResKey("VY_PODALI_ZAPROS_NA_SVYAZ_S_PREDSTAVITELE");

    final QuestOption QO_SCAN = new QuestOption(SpasholeAssets.textResources.get(SKANIROVAT_BLIZHNII_KOSMOS),
            {
                skipComputeState()
                text(SpasholeAssets.textResources.get(REZUL_TATY_SKANIROVANIYA_SOLNECHNOI_SISTEM))
            });
    final QuestOption QO_CHECK_SYS = new QuestOption(SpasholeAssets.textResources.get(PROVERIT_SISTEMY),
            {
                skipComputeState()
                if (Scenario.warpEngineInstalled) {
                    text SpasholeAssets.textResources.get(VSE_SISTEMY_V_NORME)
                } else {
                    text SpasholeAssets.textResources.get(VY_PROVODITE_BYSTRYI_OSMOTR_SISTEM_NGIPE)
                }
            });

    final QuestOption QO10 = new QuestOption(SpasholeAssets.textResources.get(PROVERIT_GIPER_DVIGATEL),
            {
                skipComputeState()
                text(SpasholeAssets.textResources.get(GIPER_DVIGATEL_RAZRUSHEN_VY_NE_MOZHETE_E))
            });

    private Entity player
    boolean onShip
    int CALL_WITH_PLANET = 10
    int EXIT_FROM_GALAXY = 100

    public PlayerActionsDialog(Entity player, DialogWindow questWindow) {
        super(questWindow);
        this.player = player
        if (ShipManager.components.has(player)) {
            onShip = true
        } else if (HumanManager.components.has(player)) {
            onShip = false
        } else {
            throw new GdxRuntimeException("Player state unknown! E: " + NameManager.getSysName(player.id));
        }

    }
    public static final ResKey LETET_V_RODNUU_GALAKTIKU = new ResKey("LETET_V_RODNUU_GALAKTIKU"); ;

    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [
                (QUEST_INIT_STATE)  :
                        [
                                {
                                    if (onShip) {
                                        text(SpasholeAssets.textResources.get(VY_V_KORABLE_VASHI_DEISTVIYA))
                                        text(SpasholeAssets.textResources.get(PLANETS_IN_RADIUS_OF_CONTACT)
                                                + PlayerInfoUpdateSystem.visiblePlanets.size)
                                        if (Scenario.warpEngineInstalled) {
                                            option(SpasholeAssets.textResources.get(LETET_V_RODNUU_GALAKTIKU), {
                                                newState(EXIT_FROM_GALAXY)
                                                Scenario.goHomeState(this)
                                            })
                                        }
                                        option(QO_SCAN)
                                        option(QO_CHECK_SYS)
                                        PlayerInfoUpdateSystem.visiblePlanets.each { planetUUid ->
                                            def planetEntity = SpasholeApp.ARTEMIS_UUIDS.getEntity(planetUUid)
                                            def name = NameManager.getName(planetEntity.id)
                                            def opt = new PlanetOption(SpasholeAssets.textResources.get(CALL_PLANET)
                                                    + ' ' + name,
                                                    {
                                                        newState(CALL_WITH_PLANET)
                                                    })
                                            opt.planetE = planetEntity
                                            opt.name = name
                                            option(opt)
                                        }
                                        option(QUEST_OPTION_EXIT)
                                    } else {
                                        text(SpasholeAssets.textResources.get(VY_V_OTKRYTOM_KOSMOSE_VASHI_DEISTVIYA))
                                        option(QUEST_OPTION_EXIT)
                                    }
                                }
                        ]
                , (CALL_WITH_PLANET):
                        [
                                {
                                    def planetOption = lastSelectedOption as PlanetOption
                                    SpasholeDialog dialogPrepared = EntityDialogManager.getDialog(planetOption.planetE)
                                    if (dialogPrepared) {
                                        complete()
                                        AbstractDialog.start(dialogPrepared)
                                    } else {
                                        // contact impossible
                                        text(SpasholeAssets.textResources.get(VY_PODALI_ZAPROS_NA_SVYAZ_S_PREDSTAVITELE
                                                , planetOption.name))
                                        newState(QUEST_INIT_STATE)
                                        reCompute()
                                    }
                                }
                        ]

                , (EXIT_FROM_GALAXY):
                        [
                                // state already handled
                        ]

        ]
    }

    @Override
    protected String initSpeakerName() {
        return null
    }

    static class PlanetOption extends QuestOption {
        Entity planetE
        String name

        PlanetOption(String text, Closure actionClosure) {
            super(text, actionClosure)
        }
    }
}
