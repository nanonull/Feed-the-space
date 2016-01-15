package conversion7.spashole.game.story.dialogs

import com.artemis.Entity
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.story.Scenario
import conversion7.spashole.game.story.SpasholeDialog

class GrokShipFirstDialog extends SpasholeDialog{
    private Entity entId

    GrokShipFirstDialog(Entity entId, DialogWindow questWindow) {
        super(questWindow)
        this.entId = entId
    }
    public static final ResKey GROK = new ResKey("GROK");

    @Override
    protected String initSpeakerName() {
        return SpasholeAssets.textResources.get(GROK)
    }

    // RESOURCE KEYS:
    static final ResKey VY_VIDITE_KORABL_RASY_GROKOV = new ResKey('GROK_SHIP_DIALOG_VY_VIDITE_KORABL_RASY_GROKOV')
    static final ResKey VY_VIDITE_KORABL_NEIZVESTNOI_VAM_RASY = new ResKey('GROK_SHIP_DIALOG_VY_VIDITE_KORABL_NEIZVESTNOI_VAM_RASY')
    static final ResKey SCAN = new ResKey('GROK_SHIP_DIALOG_SCAN')
    static final ResKey SKANER_NICHEGO_NE_OBNARUZHIL_VIDIMO_NE_PROBIVAETSYA_S = new ResKey('GROK_SHIP_DIALOG_SKANER_NICHEGO_NE_OBNARUZHIL_VIDIMO_NE_PROBIVAETSYA_S')
    // STATES:
    // CLOSURES:

    /** DIALOG STATES DEFINITION MAP */@Override
    protected Map<Object, List> getQuestStateMap() {
        return [(QUEST_INIT_STATE):
                        [
                                {
                                    disableSpeaker()
                                    if (Scenario.GrokGlobals.playerKnowsAboutGroks)
                                        text(SpasholeAssets.textResources.get(VY_VIDITE_KORABL_RASY_GROKOV))
                                    else
                                        text(SpasholeAssets.textResources.get(VY_VIDITE_KORABL_NEIZVESTNOI_VAM_RASY))
                                    Scenario.GrokGlobals.playerSawGrokShip()
                                    option(SpasholeAssets.textResources.get(SCAN), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(SKANER_NICHEGO_NE_OBNARUZHIL_VIDIMO_NE_PROBIVAETSYA_S))
                                    })
                                    option(QUEST_OPTION_EXIT)
                                    focusOn(SpasholeApp.ARTEMIS_UUIDS.getUuid(entId))
                                    setPictureViewActive(false)
                                }
                        ]


        ]
    }


}
