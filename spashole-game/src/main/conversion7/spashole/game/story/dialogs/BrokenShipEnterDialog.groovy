package conversion7.spashole.game.story.dialogs

import com.artemis.Entity
import conversion7.gdxg.core.dialog.QuestOption
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.artemis_odb.systems.ActivationTriggerManager
import conversion7.spashole.game.artemis_odb.systems.ShipManager
import conversion7.spashole.game.story.Journal
import conversion7.spashole.game.story.Scenario
import conversion7.spashole.game.story.SpasholeDialog

public class BrokenShipEnterDialog extends SpasholeDialog {
    public static
    final ResKey POPYTAT_SYA_SILOI_PRONIKNUT_NA_KORABL = new ResKey("POPYTAT_SYA_SILOI_PRONIKNUT_NA_KORABL");

    final QuestOption QUEST_OPTION_1 = new QuestOption(SpasholeAssets.textResources.get(POPYTAT_SYA_SILOI_PRONIKNUT_NA_KORABL),
            {
                newState(STATE1)
                Entity shipE = SpasholeApp.ARTEMIS_UUIDS.getEntity(ship)
                ActivationTriggerManager.components.remove(
                        shipE
                )
                def activatedByEntity = SpasholeApp.ARTEMIS_UUIDS.getUuid(activatedBy)
                ShipManager.setPilot(activatedByEntity, shipE);

            });
    public static final ResKey NE_RISKOVAT_UITI = new ResKey("NE_RISKOVAT_UITI");
    final QuestOption QUEST_OPTION_2 = new QuestOption(SpasholeAssets.textResources.get(NE_RISKOVAT_UITI),
            {
                newState(QUEST_COMPLETED_STATE)
            });

    public static final ResKey ZAVERSHIT = new ResKey("ZAVERSHIT");
    public static
    final ResKey YA_NASHEL_KORABL_S_PLANETY_LUMEN_EGO_CEL = new ResKey("YA_NASHEL_KORABL_S_PLANETY_LUMEN_EGO_CEL");
    final QuestOption QUEST_OPTION_3 = new QuestOption(SpasholeAssets.textResources.get(ZAVERSHIT),
            {
                newState(QUEST_COMPLETED_STATE)
                Journal.addToJournal(
                        SpasholeAssets.textResources.get(YA_NASHEL_KORABL_S_PLANETY_LUMEN_EGO_CEL
                                , Scenario.LumenGlobals.LUMEN_MAIN_PLANET_POS))
            });


    int STATE1 = 1
    private Entity activatedBy
    private UUID ship

    public BrokenShipEnterDialog(Entity activatedBy, UUID ship, DialogWindow questWindow) {
        super(questWindow);
        this.ship = ship
        this.activatedBy = activatedBy
    }

    public static final ResKey VBLIZI_KORABL_VYGLYADIT_BEZNADEZHNO_RAZRU = new ResKey("VBLIZI_KORABL_VYGLYADIT_BEZNADEZHNO_RAZRU");
    public static final ResKey VY_PRONIKLI_NA_KORABL_TAM_VY_NAHODITE_PO = new ResKey("VY_PRONIKLI_NA_KORABL_TAM_VY_NAHODITE_PO");


    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [
                (QUEST_INIT_STATE):
                        [
                                SpasholeAssets.textResources.get(VBLIZI_KORABL_VYGLYADIT_BEZNADEZHNO_RAZRU)
                                , {
                                    setPictureViewActive(false)
                                }
                                , QUEST_OPTION_1
                                , QUEST_OPTION_2
                        ]
                , (STATE1)        :
                        [
                                SpasholeAssets.textResources.get(VY_PRONIKLI_NA_KORABL_TAM_VY_NAHODITE_PO, Scenario.LumenGlobals.LUMEN_MAIN_PLANET_POS)
                                , QUEST_OPTION_3

                        ]
        ]
    }

    @Override
    protected String initSpeakerName() {
        return null
    }
}
