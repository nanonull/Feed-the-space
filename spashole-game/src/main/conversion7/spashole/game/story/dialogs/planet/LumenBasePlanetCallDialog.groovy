package conversion7.spashole.game.story.dialogs.planet

import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.story.Scenario

abstract class LumenBasePlanetCallDialog extends CommonPlanetCallDialog {

    LumenBasePlanetCallDialog(DialogWindow questWindow, UUID planetUuid) {
        super(questWindow, planetUuid)
    }

    // RESOURCE KEYS:
    public static
    final ResKey VOT_KOORDINATY_PLANETY_GDE_MY_VIDELI_IH_P = new ResKey('LUMEN_PLANET_DIALOG_VOT_KOORDINATY_PLANETY_GDE_MY_VIDELI_IH_P')
    public static
    final ResKey WE_D_LIKE_TO_HELP_YOU_BUT_WE_HAVE_NO_SUCH = new ResKey('LUMEN_PLANET_DIALOG_WE_D_LIKE_TO_HELP_YOU_BUT_WE_HAVE_NO_SUCH')
    public static final ResKey RASSKAZHI_O_GROKAH = new ResKey('LUMEN_PLANET_DIALOG_RASSKAZHI_O_GROKAH')
    public static
    final ResKey ONI_PRIBYLI_SUDA10_MEGASEKUND_NAZAD_OTKU = new ResKey('ONI_PRIBYLI_SUDA10_MEGASEKUND_NAZAD_OTKU')
    public static
    final ResKey KAK_VY_TOGDA_PEREHVATILI_MENYA_V_GIPERE = new ResKey('LUMEN_PLANET_DIALOG_KAK_VY_TOGDA_PEREHVATILI_MENYA_V_GIPERE')
    public static
    final ResKey DLYA_ETOGO_NAM_NE_NUZHNO_BYLO_TUDA_LETET = new ResKey('LUMEN_PLANET_DIALOG_DLYA_ETOGO_NAM_NE_NUZHNO_BYLO_TUDA_LETET')
    public static final ResKey GDE_NAITI_GROKOV = new ResKey('LUMEN_PLANET_DIALOG_GDE_NAITI_GROKOV')
    public static final ResKey HI_I_HAVE_A_LOT_TO_TELL_YOU = new ResKey('LUMEN_PLANET_DIALOG_HI_I_HAVE_A_LOT_TO_TELL_YOU')
    public static final ResKey WHAT_DO_YOU_WANT_TO_TELL_ME = new ResKey('LUMEN_PLANET_DIALOG_WHAT_DO_YOU_WANT_TO_TELL_ME')
    public static final ResKey MY_HYPER_ENGINE_WAS_BROKEN = new ResKey('LUMEN_PLANET_DIALOG_MY_HYPER_ENGINE_WAS_BROKEN')
    public static final ResKey WHO_ARE_YOU = new ResKey('LUMEN_PLANET_DIALOG_WHO_ARE_YOU')
    public static
    final ResKey I_AM_AN_OFFICIAL_REPRESENTATIVE_OF_THE_MAI = new ResKey('LUMEN_PLANET_DIALOG_I_AM_AN_OFFICIAL_REPRESENTATIVE_OF_THE_MAI')
    public static final ResKey GDE_YA = new ResKey('LUMEN_PLANET_DIALOG_GDE_YA')
    public static
    final ResKey TY_V_AVTONOMNOI_SOLNECHNOI_SISTEME_DORA_LI = new ResKey('LUMEN_PLANET_DIALOG_TY_V_AVTONOMNOI_SOLNECHNOI_SISTEME_DORA_LI')
    public static
    final ResKey ETO_MY_PEREHVATILI_TEBYA_V_GIPER_PROSTRANS = new ResKey('LUMEN_PLANET_DIALOG_ETO_MY_PEREHVATILI_TEBYA_V_GIPER_PROSTRANS')
    public static final ResKey PODROBNEE = new ResKey('LUMEN_PLANET_DIALOG_PODROBNEE')
    public static final ResKey YA_POMOGU = new ResKey('LUMEN_PLANET_DIALOG_YA_POMOGU')
    public static final ResKey SAMI_NE_SPRAVITES = new ResKey('LUMEN_PLANET_DIALOG_SAMI_NE_SPRAVITES')
    public static
    final ResKey NUZHNA_POMOSH_TRET_EI_STORONY_MOGU_RASSK = new ResKey('LUMEN_PLANET_DIALOG_NUZHNA_POMOSH_TRET_EI_STORONY_MOGU_RASSK')
    public static final ResKey SPRASHIVAI = new ResKey('LUMEN_PLANET_DIALOG_SPRASHIVAI')
    public static final ResKey RASSKAZHI_O_LUMENAH = new ResKey('LUMEN_PLANET_DIALOG_RASSKAZHI_O_LUMENAH')
    public static
    final ResKey MY_SAMAYA_MIROLUBIVAYA_RASA_V_GALAKTIKE_N = new ResKey('LUMEN_PLANET_DIALOG_MY_SAMAYA_MIROLUBIVAYA_RASA_V_GALAKTIKE_N')
    public static
    final ResKey DAZHE_NEZNAU_KAK_TEBYA_BLAGODARIT = new ResKey('LUMEN_PLANET_DIALOG_DAZHE_NEZNAU_KAK_TEBYA_BLAGODARIT')
    public static final ResKey POPROBUI_RAZVEDAT_ETO_MESTO = new ResKey('LUMEN_PLANET_DIALOG_POPROBUI_RAZVEDAT_ETO_MESTO')
    public static
    final ResKey HI_PLEASE_CONTACT_OUR_MAIN_PLANET_WE_HAVE_VERY_IMPOR = new ResKey('LUMEN_PLANET_DIALOG_HI_PLEASE_CONTACT_OUR_MAIN_PLANET_WE_HAVE_VERY_IMPOR')
    // STATES:
    public static final UUID MY_HYPER_ENGINE_WAS_BROKEN_STATE = UUID.randomUUID()
    public static final UUID WHAT_DO_YOU_WANT_TO_TELL_ME_STATE = UUID.randomUUID()
    public static final UUID PODROBNEE_STATE = UUID.randomUUID()
    public static final UUID YA_POMOGU_STATE = UUID.randomUUID()
    public static final UUID COMMON_PLANET_STATE = UUID.randomUUID()
    // CLOSURES:
    Closure votKoordinatyPlanetyGdeMyVideliIhPoslednii =
            {
                owner.text(SpasholeAssets.textResources.get(VOT_KOORDINATY_PLANETY_GDE_MY_VIDELI_IH_P
                        , Scenario.GrokGlobals.MAIN_PLANET_POS))
            }
    public static final ResKey HI = new ResKey('LUMEN_PLANET_DIALOG_HI')

    /** DIALOG STATES DEFINITION MAP */
    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [
                (MY_HYPER_ENGINE_WAS_BROKEN_STATE)   :
                        [
                                {
                                    text(SpasholeAssets.textResources.get(WE_D_LIKE_TO_HELP_YOU_BUT_WE_HAVE_NO_SUCH))
                                    option(SpasholeAssets.textResources.get(WHAT_DO_YOU_WANT_TO_TELL_ME), {
                                        newState(WHAT_DO_YOU_WANT_TO_TELL_ME_STATE)
                                    })
                                    option(SpasholeAssets.textResources.get(RASSKAZHI_O_GROKAH), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(ONI_PRIBYLI_SUDA10_MEGASEKUND_NAZAD_OTKU))
                                        Scenario.GrokGlobals.playerKnowsAboutGroks()
                                    })
                                    if (Scenario.LumenGlobals.playerKnowsLumensCatchHimInHyper) {
                                        option(SpasholeAssets.textResources.get(KAK_VY_TOGDA_PEREHVATILI_MENYA_V_GIPERE), {
                                            skipComputeState()
                                            text(SpasholeAssets.textResources.get(DLYA_ETOGO_NAM_NE_NUZHNO_BYLO_TUDA_LETET))
                                        })
                                    }
                                    option(SpasholeAssets.textResources.get(GDE_NAITI_GROKOV), {
                                        skipComputeState()
                                        votKoordinatyPlanetyGdeMyVideliIhPoslednii.call()
                                    })
                                    option(QUEST_OPTION_EXIT)
                                }
                        ]
                , (COMMON_PLANET_STATE)              :
                        [
                                {
                                    enableSpeaker()
                                    if (Scenario.LumenGlobals.acceptedQuestHelpWithGroks) {
                                        text(SpasholeAssets.textResources.get(HI))
                                        option(SpasholeAssets.textResources.get(PODROBNEE), {
                                            newState(PODROBNEE_STATE)
                                        })
                                    } else {
                                        text(SpasholeAssets.textResources.get(HI_PLEASE_CONTACT_OUR_MAIN_PLANET_WE_HAVE_VERY_IMPOR))
                                    }
                                    option(QUEST_OPTION_EXIT)
                                }
                        ]
                , (QUEST_INIT_STATE)                 :
                        [
                                {
                                    enableSpeaker()
                                    text(SpasholeAssets.textResources.get(HI_I_HAVE_A_LOT_TO_TELL_YOU))
                                    option(SpasholeAssets.textResources.get(WHAT_DO_YOU_WANT_TO_TELL_ME), {
                                        newState(WHAT_DO_YOU_WANT_TO_TELL_ME_STATE)
                                    })
                                    option(SpasholeAssets.textResources.get(MY_HYPER_ENGINE_WAS_BROKEN), {
                                        newState(MY_HYPER_ENGINE_WAS_BROKEN_STATE)
                                    })
                                    option(SpasholeAssets.textResources.get(WHO_ARE_YOU), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(I_AM_AN_OFFICIAL_REPRESENTATIVE_OF_THE_MAI))
                                    })
                                    option(SpasholeAssets.textResources.get(GDE_YA), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(TY_V_AVTONOMNOI_SOLNECHNOI_SISTEME_DORA_LI))
                                    })
                                }
                        ]

                , (WHAT_DO_YOU_WANT_TO_TELL_ME_STATE):
                        [
                                {
                                    text(SpasholeAssets.textResources.get(ETO_MY_PEREHVATILI_TEBYA_V_GIPER_PROSTRANS))
                                    option(SpasholeAssets.textResources.get(PODROBNEE), {
                                        newState(PODROBNEE_STATE)
                                    })
                                    if (!Scenario.LumenGlobals.acceptedQuestHelpWithGroks) {
                                        option(SpasholeAssets.textResources.get(YA_POMOGU), {
                                            newState(YA_POMOGU_STATE)
                                        })
                                    }
                                    option(SpasholeAssets.textResources.get(SAMI_NE_SPRAVITES), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(NUZHNA_POMOSH_TRET_EI_STORONY_MOGU_RASSK))
                                    })
                                    option(QUEST_OPTION_EXIT)
                                }
                        ]

                , (PODROBNEE_STATE)                  :
                        [
                                {
                                    enableSpeaker()
                                    text(SpasholeAssets.textResources.get(SPRASHIVAI))
                                    option(SpasholeAssets.textResources.get(RASSKAZHI_O_LUMENAH), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(MY_SAMAYA_MIROLUBIVAYA_RASA_V_GALAKTIKE_N))
                                    })
                                    option(SpasholeAssets.textResources.get(RASSKAZHI_O_GROKAH), {
                                        skipComputeState()
                                        text(SpasholeAssets.textResources.get(ONI_PRIBYLI_SUDA10_MEGASEKUND_NAZAD_OTKU))
                                        Scenario.GrokGlobals.playerKnowsAboutGroks()

                                    })
                                    if (!Scenario.LumenGlobals.acceptedQuestHelpWithGroks) {
                                        option(SpasholeAssets.textResources.get(YA_POMOGU), {
                                            newState(YA_POMOGU_STATE)
                                        })
                                    }
                                    option(SpasholeAssets.textResources.get(GDE_NAITI_GROKOV), {
                                        skipComputeState()
                                        votKoordinatyPlanetyGdeMyVideliIhPoslednii.call()
                                    })
                                    option(QUEST_OPTION_EXIT)
                                }
                        ]

                , (YA_POMOGU_STATE)                  :
                        [
                                {
                                    text(SpasholeAssets.textResources.get(DAZHE_NEZNAU_KAK_TEBYA_BLAGODARIT))
                                    votKoordinatyPlanetyGdeMyVideliIhPoslednii.call()
                                    text(SpasholeAssets.textResources.get(POPROBUI_RAZVEDAT_ETO_MESTO))
                                    Scenario.LumenGlobals.acceptQuestHelpWithGroks()
                                    option(SpasholeAssets.textResources.get(PODROBNEE), {
                                        newState(PODROBNEE_STATE)
                                    })
                                    option(QUEST_OPTION_EXIT)
                                }
                        ]


        ]
    }
}
