package conversion7.spashole.game.story.dialogs

import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.artemis_odb.systems.EntityInventoryManager
import conversion7.spashole.game.artemis_odb.systems.InventoryItemComp
import conversion7.spashole.game.story.Scenario
import conversion7.spashole.game.story.SpasholeDialog

class GetWarpEngineDialog extends SpasholeDialog {

    private int shipId

    GetWarpEngineDialog(int shipId, DialogWindow questWindow) {
        super(questWindow)
        this.shipId = shipId
    }

    @Override
    protected String initSpeakerName() {
        return null
    }

    // RESOURCE KEYS:
    static
    final ResKey VY_NAHODITE_NEPOVREZHDENNYI_GIPER_DVIGATEL = new ResKey('GET_WARP_ENGINE_DIALOG_VY_NAHODITE_NEPOVREZHDENNYI_GIPER_DVIGATEL')
    static final ResKey USTANOVIT = new ResKey('GET_WARP_ENGINE_DIALOG_USTANOVIT')
    static final ResKey OSMOTRET = new ResKey('GET_WARP_ENGINE_DIALOG_OSMOTRET')
    static
    final ResKey KACHESTVO_DVIGATELYA_KAK_V_LUCHSHIH_MAGAZINAH_VASHEI_P = new ResKey('GET_WARP_ENGINE_DIALOG_KACHESTVO_DVIGATELYA_KAK_V_LUCHSHIH_MAGAZINAH_VASHEI_P')
    static
    final ResKey DVIGATEL_USTANOVLEN_DIAGNOSTIKA_NE_OBNARUZHILA_OSHIB = new ResKey('GET_WARP_ENGINE_DIALOG_DVIGATEL_USTANOVLEN_DIAGNOSTIKA_NE_OBNARUZHILA_OSHIB')
    static final ResKey LETET_DOMOI = new ResKey('GET_WARP_ENGINE_DIALOG_LETET_DOMOI')
    // STATES:
    static final UUID USTANOVIT_STATE = UUID.randomUUID()
    static final UUID LETET_DOMOI_STATE = UUID.randomUUID()
    // CLOSURES:

    /** DIALOG STATES DEFINITION MAP */
    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [(QUEST_INIT_STATE)   :
                        [
                                {
                                    disableSpeaker()
                                    text(SpasholeAssets.textResources.get(VY_NAHODITE_NEPOVREZHDENNYI_GIPER_DVIGATEL))
                                    option(SpasholeAssets.textResources.get(USTANOVIT), {
                                        newState(USTANOVIT_STATE)
                                    })
                                    option(SpasholeAssets.textResources.get(OSMOTRET), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(KACHESTVO_DVIGATELYA_KAK_V_LUCHSHIH_MAGAZINAH_VASHEI_P))
                                    })
                                }
                        ]

                , (USTANOVIT_STATE)  :
                        [
                                {
                                    text(SpasholeAssets.textResources.get(DVIGATEL_USTANOVLEN_DIAGNOSTIKA_NE_OBNARUZHILA_OSHIB))
//                                    remove engine from inventory
                                    EntityInventoryManager.components.get(shipId).removeItem(InventoryItemComp.Type.WARP_ENGINE)
                                    Scenario.warpEngineInstall()
                                    option(QUEST_OPTION_EXIT)
                                    option(SpasholeAssets.textResources.get(LETET_DOMOI), {
                                        newState(LETET_DOMOI_STATE)
                                    })
                                }
                        ]

                , (LETET_DOMOI_STATE):
                        [
                                {
                                    Scenario.goHomeState(this)
                                }
                        ]


        ]
    }


}
