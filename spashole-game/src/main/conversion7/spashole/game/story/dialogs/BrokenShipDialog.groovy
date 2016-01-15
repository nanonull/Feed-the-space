package conversion7.spashole.game.story.dialogs

import conversion7.gdxg.core.dialog.QuestOption
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.story.SpasholeDialog

public class BrokenShipDialog extends SpasholeDialog {
    public static final ResKey RAZVEDAT = new ResKey("RAZVEDAT");
    public static final ResKey VALIM_OTSUDA = new ResKey("VALIM_OTSUDA");
    public static
    final ResKey VY_VIDITE_KOSMICHESKII_KORABL_NIKAKIH_SI = new ResKey("VY_VIDITE_KOSMICHESKII_KORABL_NIKAKIH_SI");
    final QuestOption QUEST_OPTION_1 = new QuestOption(SpasholeAssets.textResources.get(RAZVEDAT),
            {
                newState(QUEST_COMPLETED_STATE)
            });
    final QuestOption QUEST_OPTION_2 = new QuestOption(SpasholeAssets.textResources.get(VALIM_OTSUDA),
            {
                newState(QUEST_COMPLETED_STATE)
            });
    UUID shipUuid


    public BrokenShipDialog(int shipId, DialogWindow questWindow) {
        super(questWindow);
        this.shipUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(
                SpasholeApp.ARTEMIS_ENGINE.getEntity(shipId))
    }

    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [
                (QUEST_INIT_STATE):
                        [
                                SpasholeAssets.textResources.get(VY_VIDITE_KOSMICHESKII_KORABL_NIKAKIH_SI)
                                , QUEST_OPTION_1
                                , QUEST_OPTION_2
                                , {
                                    setPictureViewActive(false)
                                    focusOn(shipUuid)
                                }
                        ]

        ]
    }

    @Override
    protected String initSpeakerName() {
        return null
    }
}
