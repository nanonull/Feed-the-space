package conversion7.spashole.game.story.dialogs
import com.artemis.Entity
import conversion7.gdxg.core.dialog.QuestOption
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.audio.AudioPlayer
import conversion7.spashole.game.story.Journal

public class IntroDialog extends PlayerActionsDialog {

    public static final ResKey PRODOLZHIT = new ResKey("PRODOLZHIT");
    public static
    final ResKey POSLE_GIPER_PRYZHKA_YA_OKAZALSYA_V_NEIZVES = new ResKey("POSLE_GIPER_PRYZHKA_YA_OKAZALSYA_V_NEIZVES");
    public static final ResKey PROVESTI_OSMOTR_SISTEM = new ResKey("PROVESTI_OSMOTR_SISTEM");
    public static final ResKey AKTIVIROVAT_SISTEMY_UPRAVLENIYA = new ResKey("AKTIVIROVAT_SISTEMY_UPRAVLENIYA");
    public static final ResKey AKTIVIROVAT_SHTURVAL = new ResKey("AKTIVIROVAT_SHTURVAL");
    public static
    final ResKey VY_PRIHODITE_V_SEBYA_POSLEDNEE_CHTO_VY = new ResKey("VY_PRIHODITE_V_SEBYA_POSLEDNEE_CHTO_VY");
    public static
    final ResKey VY_BROSAETE_VZGLYAD_NA_BORTOVUU_PANEL_KO = new ResKey("VY_BROSAETE_VZGLYAD_NA_BORTOVUU_PANEL_KO");
    public static
    final ResKey VY_PROVODITE_OSMOTR_SISTEM_NGIPER_DVIGAT = new ResKey("VY_PROVODITE_OSMOTR_SISTEM_NGIPER_DVIGAT");
    public static
    final ResKey SISTEMY_UPRAVLENIYA_AKTIVIROVANY_I_RABOTAU = new ResKey("SISTEMY_UPRAVLENIYA_AKTIVIROVANY_I_RABOTAU");

    final QuestOption QUEST_OPTION_1 = new QuestOption(SpasholeAssets.textResources.get(PRODOLZHIT),
            {
                newState(STATE1)
                Journal.addToJournal(SpasholeAssets.textResources.get(POSLE_GIPER_PRYZHKA_YA_OKAZALSYA_V_NEIZVES))
            });

    final QuestOption QO_CHECK_SYS_INTRO = new QuestOption(SpasholeAssets.textResources.get(PROVESTI_OSMOTR_SISTEM),
            {
                newState(2)
            });


    final QuestOption QUEST_OPTION_4_3 = new QuestOption(SpasholeAssets.textResources.get(AKTIVIROVAT_SISTEMY_UPRAVLENIYA),
            {
                newState(STATE4)
            });

    final QuestOption QUEST_OPTION_5 = new QuestOption(SpasholeAssets.textResources.get(AKTIVIROVAT_SHTURVAL),
            {
                newState(QUEST_COMPLETED_STATE)
            });


    static int STATE1 = 1
    static int STATE4 = 4

    public IntroDialog(Entity entity, DialogWindow questWindow) {
        super(entity, questWindow);
        disableSpeaker()
    }

    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [
                (QUEST_INIT_STATE):
                        [
                                {
                                    text SpasholeAssets.textResources.get(VY_PRIHODITE_V_SEBYA_POSLEDNEE_CHTO_VY)
                                    AudioPlayer.play(SpasholeAssets.ritualAudio)
                                }
                                , QUEST_OPTION_1
                        ]

                , (STATE1)        :
                        [
                                SpasholeAssets.textResources.get(VY_BROSAETE_VZGLYAD_NA_BORTOVUU_PANEL_KO)
                                , QO_SCAN
                                , QO_CHECK_SYS_INTRO
                                , {
                                    setPictureViewActive(false)
                                }
                        ]

                , (2)             :
                        [
                                SpasholeAssets.textResources.get(VY_PROVODITE_OSMOTR_SISTEM_NGIPER_DVIGAT)
                                , QO10
                                , QO_SCAN
                                , QUEST_OPTION_4_3

                        ]
                , (STATE4)        :
                        [
                                SpasholeAssets.textResources.get(SISTEMY_UPRAVLENIYA_AKTIVIROVANY_I_RABOTAU)
                                , QUEST_OPTION_5
                                , {
                                    AudioPlayer.play(SpasholeAssets.track1)
                                }
                        ]
        ]
    }

    @Override
    protected String initSpeakerName() {
        return null
    }
}
