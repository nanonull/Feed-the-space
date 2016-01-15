package conversion7.spashole.game.story.dialogs

import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.story.SpasholeDialog

class TestDialog1 extends SpasholeDialog {

    TestDialog1(DialogWindow questWindow) {
        super(questWindow)
    }

    @Override
    protected String initSpeakerName() {
        return 'Speaker'
    }
// RESOURCE KEYS:
    static
    final ResKey VOT_KOORDINATY_PLANETY_GDE_MY_VIDELI_IH_P = new ResKey('TEST_DIALOG_VOT_KOORDINATY_PLANETY_GDE_MY_VIDELI_IH_P')
    static final ResKey HI_THERE = new ResKey('TEST_DIALOG_HI_THERE')
    static final ResKey I_NEMNOZHKO_KIRILLICY = new ResKey('TEST_DIALOG_I_NEMNOZHKO_KIRILLICY')
    static final ResKey HI = new ResKey('TEST_DIALOG_HI')
    static final ResKey HAVE_YOU_A_QUESTION = new ResKey('TEST_DIALOG_HAVE_YOU_A_QUESTION')
    static final ResKey QUESTION = new ResKey('TEST_DIALOG_QUESTION')
    static final ResKey YES_PEOPLE_OFTEN_ASK_SOMETHING = new ResKey('TEST_DIALOG_YES_PEOPLE_OFTEN_ASK_SOMETHING')
    static final ResKey EXECUTE_CLOSURE = new ResKey('TEST_DIALOG_EXECUTE_CLOSURE')
    static final ResKey EXECUTED_CLOSURE = new ResKey('TEST_DIALOG_EXECUTED_CLOSURE')
    // STATES:
    static final UUID HI_STATE = UUID.randomUUID()
    // CLOSURES:
    Closure votKoordinatyPlanetyGdeMyVideliIhPoslednii =
            {
                owner.text(SpasholeAssets.textResources.get(VOT_KOORDINATY_PLANETY_GDE_MY_VIDELI_IH_P))

            }

    /** DIALOG STATES DEFINITION MAP */
    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [
                (QUEST_INIT_STATE):
                        [
                                {
                                    enableSpeaker()
                                    text(SpasholeAssets.textResources.get(HI_THERE))
                                    disableSpeaker()
                                    text(SpasholeAssets.textResources.get(I_NEMNOZHKO_KIRILLICY))
                                    enableSpeaker()
                                    votKoordinatyPlanetyGdeMyVideliIhPoslednii.call()
                                    option(SpasholeAssets.textResources.get(HI), {
                                        newState(HI_STATE)
                                    })
                                }
                        ]

                , (HI_STATE)      :
                        [
                                {
                                    text(SpasholeAssets.textResources.get(HAVE_YOU_A_QUESTION))
                                    votKoordinatyPlanetyGdeMyVideliIhPoslednii.call()
                                    option(SpasholeAssets.textResources.get(QUESTION), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(YES_PEOPLE_OFTEN_ASK_SOMETHING))
                                    })
                                    option(QUEST_OPTION_EXIT)
                                    option(SpasholeAssets.textResources.get(EXECUTE_CLOSURE), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(EXECUTED_CLOSURE))
                                        votKoordinatyPlanetyGdeMyVideliIhPoslednii.call()
                                    })
                                }
                        ]


        ]
    }
}
