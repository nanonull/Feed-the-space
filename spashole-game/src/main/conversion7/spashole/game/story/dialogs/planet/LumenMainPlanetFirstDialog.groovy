package conversion7.spashole.game.story.dialogs.planet

import conversion7.gdxg.core.dialog.QuestOption
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.story.Scenario

class LumenMainPlanetFirstDialog extends CommonPlanetCallDialog {

    public static
    final ResKey PROSKANIROVAT_INFORMACIONNOE_POLE_PLANETY = new ResKey("PROSKANIROVAT_INFORMACIONNOE_POLE_PLANETY");
    public static final ResKey PRINYAT_ZAPROS_NA_SVYAZ = new ResKey("PRINYAT_ZAPROS_NA_SVYAZ");
    final QuestOption QUEST_OPTION_1 = new QuestOption(SpasholeAssets.textResources.get(PROSKANIROVAT_INFORMACIONNOE_POLE_PLANETY),
            {
                newState(STATE1)
            });

    final QuestOption QUEST_OPTION_2 = new QuestOption(SpasholeAssets.textResources.get(PRINYAT_ZAPROS_NA_SVYAZ),
            {
                newState(STATE2)
            });
    public static final ResKey NE_IDTI_NA_KONTAKT_VYITI = new ResKey("NE_IDTI_NA_KONTAKT_VYITI");
    final QuestOption QUEST_OPTION_3 = new QuestOption(SpasholeAssets.textResources.get(NE_IDTI_NA_KONTAKT_VYITI),
            {
                newState(STATE3_CANCEL_CONTACT)
                Scenario.LumenGlobals.contactAcceptedWithLumenPlanet = false
            });

    public static
    final ResKey ZDRAVSTVUITE_KTO_VY_I_POCHEMU_MENYA_ZHDET = new ResKey("ZDRAVSTVUITE_KTO_VY_I_POCHEMU_MENYA_ZHDET");
    public static
    final ResKey VAM_TOZHE_PEREDAVALI_PRIVET_A_TEPER_YA_S = new ResKey("VAM_TOZHE_PEREDAVALI_PRIVET_A_TEPER_YA_S");
    public static final ResKey MOLCHA_ZHDAT = new ResKey("MOLCHA_ZHDAT");
    public static final ResKey MOLCHA_OTKLUCHIT_SVYAZ = new ResKey("MOLCHA_OTKLUCHIT_SVYAZ");
    public static final ResKey TY_KTO_TAKOI = new ResKey("TY_KTO_TAKOI");
    public static final ResKey KAK_VY_POSMELI = new ResKey("KAK_VY_POSMELI");
    public static final ResKey POTRUDIS_OB_YASNIT = new ResKey("POTRUDIS_OB_YASNIT");
    public static final ResKey PRODOLZHAI = new ResKey("PRODOLZHAI");
    public static
    final ResKey MENYA_ETO_NE_INTERESUET_VY_SLOMALI_MNE_GI = new ResKey("MENYA_ETO_NE_INTERESUET_VY_SLOMALI_MNE_GI");
    public static final ResKey KTO_VAM_UGROZHAET = new ResKey("KTO_VAM_UGROZHAET");
    public static final ResKey PROSTO_VERNITE_MNE_GIPER_DVIGATEL = new ResKey("PROSTO_VERNITE_MNE_GIPER_DVIGATEL");

    //
    final QuestOption QUEST_OPTION_10 = new QuestOption(SpasholeAssets.textResources.get(ZDRAVSTVUITE_KTO_VY_I_POCHEMU_MENYA_ZHDET),
            {
                newState(STATE10)
            });
    final QuestOption QUEST_OPTION_11 = new QuestOption(SpasholeAssets.textResources.get(VAM_TOZHE_PEREDAVALI_PRIVET_A_TEPER_YA_S),
            {
                newState(STATE11)
            });
    final QuestOption QUEST_OPTION_12 = new QuestOption(SpasholeAssets.textResources.get(MOLCHA_ZHDAT),
            {
                newState(STATE12)
            });
    final QuestOption QUEST_OPTION_13 = new QuestOption(SpasholeAssets.textResources.get(MOLCHA_OTKLUCHIT_SVYAZ),
            {
                newState(STATE3_CANCEL_CONTACT)
            });
    final QuestOption QUEST_OPTION_14 = new QuestOption(SpasholeAssets.textResources.get(TY_KTO_TAKOI),
            {
                newState(STATE10)
            });
    //
    final QuestOption QUEST_OPTION_20 = new QuestOption(SpasholeAssets.textResources.get(KAK_VY_POSMELI),
            {
                newState(21)
            });
    final QuestOption QUEST_OPTION_21 = new QuestOption(SpasholeAssets.textResources.get(POTRUDIS_OB_YASNIT),
            {
                newState(STATE20)
            });
    final QuestOption QUEST_OPTION_22 = new QuestOption(SpasholeAssets.textResources.get(PRODOLZHAI),
            {
                newState(STATE20)
            });

    final QuestOption QUEST_OPTION_23 = new QuestOption(SpasholeAssets.textResources.get(MENYA_ETO_NE_INTERESUET_VY_SLOMALI_MNE_GI),
            {
                newState(23)
            });
    final QuestOption QUEST_OPTION_24 = new QuestOption(SpasholeAssets.textResources.get(KTO_VAM_UGROZHAET),
            {
                newState(STATE20)
            });
    final QuestOption QUEST_OPTION_25 = new QuestOption(SpasholeAssets.textResources.get(PROSTO_VERNITE_MNE_GIPER_DVIGATEL),
            {
                newState(25)
            });

    public static final ResKey KTO_ETI_GROKI = new ResKey("KTO_ETI_GROKI");
    public static final ResKey PRICHINY_BOLI = new ResKey("PRICHINY_BOLI");
    final QuestOption QUEST_OPTION_34 = new QuestOption(SpasholeAssets.textResources.get(KTO_ETI_GROKI),
            {
                newState(34)
                Scenario.GrokGlobals.playerKnowsAboutGroks()
            });
    final QuestOption QUEST_OPTION_30 = new QuestOption(SpasholeAssets.textResources.get(PRICHINY_BOLI),
            {
                newState(STATE30)
            });

    public static
    final ResKey I_VY_HOTITE_CHTOBY_YA_VAM_POMOG_V_VOINE = new ResKey("I_VY_HOTITE_CHTOBY_YA_VAM_POMOG_V_VOINE");
    public static final ResKey VY_TEPER_PLANIRUETE_VOINU = new ResKey("VY_TEPER_PLANIRUETE_VOINU");
    public static final ResKey DOPUSTIM_ETO_PRAVDA = new ResKey("DOPUSTIM_ETO_PRAVDA");
    public static final ResKey MNE_SAMOMU_NUZHNA_POMOSH = new ResKey("MNE_SAMOMU_NUZHNA_POMOSH");
    public static final ResKey YA_POMOGAU_VAM_VY_MNE_TOL_KO_TAK = new ResKey("YA_POMOGAU_VAM_VY_MNE_TOL_KO_TAK");
    public static final ResKey C_HTO_MNE_BUDET_ZA_ETO = new ResKey("C_HTO_MNE_BUDET_ZA_ETO");
    public static final ResKey U_MENYA_SLOMALSYA_GIPER_DVIGATEL = new ResKey("U_MENYA_SLOMALSYA_GIPER_DVIGATEL");
    public static final ResKey VERNITE_MENYA_V_MOU_GALAKTIKU = new ResKey("VERNITE_MENYA_V_MOU_GALAKTIKU");
    public static final ResKey VY_SLOMALI_MNE_GIPER_DVIGATEL = new ResKey("VY_SLOMALI_MNE_GIPER_DVIGATEL");
    public static
    final ResKey VY_SLOMALI_MNE_GIPER_DVIGATEL_I_YA_CHUT = new ResKey("VY_SLOMALI_MNE_GIPER_DVIGATEL_I_YA_CHUT");
    public static final ResKey LADNO_YA_POPROBUU = new ResKey("LADNO_YA_POPROBUU");
    public static final ResKey C_HTO_MNE_POMESHAET_ZABRAT_DVIGATEL = new ResKey("C_HTO_MNE_POMESHAET_ZABRAT_DVIGATEL");
    public static final ResKey VY_MNE_NADOELI_OTKLUCHIT_SYA = new ResKey("VY_MNE_NADOELI_OTKLUCHIT_SYA");
    public static final ResKey YA_POPROBUU_POMOCH_VAM = new ResKey("YA_POPROBUU_POMOCH_VAM");
    public static final ResKey I_HAVE_QUESTIONS = new ResKey("I_HAVE_QUESTIONS");

    final QuestOption QUEST_OPTION_31 = new QuestOption(SpasholeAssets.textResources.get(I_VY_HOTITE_CHTOBY_YA_VAM_POMOG_V_VOINE),
            {
                newState(STATE32)
            });
    final QuestOption QUEST_OPTION_32 = new QuestOption(SpasholeAssets.textResources.get(VY_TEPER_PLANIRUETE_VOINU),
            {
                newState(STATE32)
            });
    final QuestOption QUEST_OPTION_33 = new QuestOption(SpasholeAssets.textResources.get(DOPUSTIM_ETO_PRAVDA),
            {
                newState(STATE31)
            });

    //

    final QuestOption QUEST_OPTION_40 = new QuestOption(SpasholeAssets.textResources.get(MNE_SAMOMU_NUZHNA_POMOSH),
            {
                newState(40)
            });

    final QuestOption QUEST_OPTION_41 = new QuestOption(SpasholeAssets.textResources.get(YA_POMOGAU_VAM_VY_MNE_TOL_KO_TAK),
            {
                newState(41)
            });

    final QuestOption QUEST_OPTION_42 = new QuestOption(SpasholeAssets.textResources.get(C_HTO_MNE_BUDET_ZA_ETO),
            {
                newState(42)
            });

    //

    final QuestOption QUEST_OPTION_50 = new QuestOption(SpasholeAssets.textResources.get(U_MENYA_SLOMALSYA_GIPER_DVIGATEL),
            {
                newState(50)
            });
    final QuestOption QUEST_OPTION_51 = new QuestOption(SpasholeAssets.textResources.get(VERNITE_MENYA_V_MOU_GALAKTIKU),
            {
                newState(51)
            });

    final QuestOption QUEST_OPTION_60 = new QuestOption(SpasholeAssets.textResources.get(VY_SLOMALI_MNE_GIPER_DVIGATEL),
            {
                newState(60)
            });
    final QuestOption QUEST_OPTION_62 = new QuestOption(SpasholeAssets.textResources.get(VY_SLOMALI_MNE_GIPER_DVIGATEL_I_YA_CHUT),
            {
                newState(62)
            });
    final QuestOption QUEST_OPTION_61 = new QuestOption(SpasholeAssets.textResources.get(LADNO_YA_POPROBUU),
            {
                newState(STATE61_Q_ACCEPTED)
            });

    final QuestOption QUEST_OPTION_70 = new QuestOption(SpasholeAssets.textResources.get(C_HTO_MNE_POMESHAET_ZABRAT_DVIGATEL),
            {
                newState(70)
            });
    final QuestOption QUEST_OPTION_71 = new QuestOption(SpasholeAssets.textResources.get(VY_MNE_NADOELI_OTKLUCHIT_SYA),
            {
                newState(70)
            });
    final QuestOption QUEST_OPTION_80 = new QuestOption(SpasholeAssets.textResources.get(YA_POPROBUU_POMOCH_VAM),
            {
                newState(STATE61_Q_ACCEPTED)
            });

    public static final ResKey MY_DADIM_TEBE_VSE_CHTO_TY_ZAHOCHESH_KRO = new ResKey("MY_DADIM_TEBE_VSE_CHTO_TY_ZAHOCHESH_KRO");
    public static final ResKey U_NAS_EST_SOVERSHENNO_NOVYI_GIPER_DVIGATE = new ResKey("U_NAS_EST_SOVERSHENNO_NOVYI_GIPER_DVIGATE");
    final QuestOption QUEST_OPTION_101 = new QuestOption(SpasholeAssets.textResources.get(I_HAVE_QUESTIONS),
            {
                newState(101)
            });

    int STATE1 = 1
    int STATE2 = 2
    int STATE3_CANCEL_CONTACT = 3
    int STATE10 = 10
    int STATE11 = 11
    int STATE12 = 12
    int STATE20 = 20
    int STATE30 = 30
    int STATE31 = 31
    int STATE32 = 32
    int STATE61_Q_ACCEPTED = 61
    static String ARTUR_LAST_ARG = SpasholeAssets.textResources.get(MY_DADIM_TEBE_VSE_CHTO_TY_ZAHOCHESH_KRO)
    static String ARTUR_WE_HAVE_ENGINE_YOU_HELP_US = SpasholeAssets.textResources.get(U_NAS_EST_SOVERSHENNO_NOVYI_GIPER_DVIGATE)

    LumenMainPlanetFirstDialog(DialogWindow questWindow, UUID planetUuid) {
        super(questWindow, planetUuid)
    }

    @Override
    protected void initQuestState() {
        super.initQuestState()
        disableSpeaker()
    }
    public static final ResKey VY_PODLETAETE_K_PLANETE_N_VASH_IONNYI_SVY = new ResKey("VY_PODLETAETE_K_PLANETE_N_VASH_IONNYI_SVY");
    public static final ResKey REZUL_TAT_SKANIROVANIYA_INFORMACIONNOGO_PO = new ResKey("REZUL_TAT_SKANIROVANIYA_INFORMACIONNOGO_PO");
    public static final ResKey NA_UDIVLENIE_SVYAZ_BYLA_USTANOVLENA_MGNO = new ResKey("NA_UDIVLENIE_SVYAZ_BYLA_USTANOVLENA_MGNO");
    public static final ResKey ZDRAVSTVUI_MY_DAVNO_TEBYA_ZHDEM = new ResKey("ZDRAVSTVUI_MY_DAVNO_TEBYA_ZHDEM");
    public static final ResKey YA_PREDSTAVITEL_RASY_LUMEN_SAMOI_MIROLUB = new ResKey("YA_PREDSTAVITEL_RASY_LUMEN_SAMOI_MIROLUB");
    public static final ResKey ETO_MY_PEREHVATILI_TEBYA_V_GIPER_PRYZHKE_I = new ResKey("ETO_MY_PEREHVATILI_TEBYA_V_GIPER_PRYZHKE_I");
    public static final ResKey S_TOI_STORONY_TOZHE_TISHINA_ZATEM_ARTUR_S = new ResKey("S_TOI_STORONY_TOZHE_TISHINA_ZATEM_ARTUR_S");
    public static final ResKey PRIEM_ETO_PLANETA_LUMEN_MY_HOTIM_USTANOV = new ResKey("PRIEM_ETO_PLANETA_LUMEN_MY_HOTIM_USTANOV");
    public static final ResKey NASHI_AGRESSIVNYE_SOSEDI_GROKI_NACHALI_A = new ResKey("NASHI_AGRESSIVNYE_SOSEDI_GROKI_NACHALI_A");
    public static final ResKey NAM_GROZIT_POLNOE_UNICHTOZHENIE_I_MY_NE_M = new ResKey("NAM_GROZIT_POLNOE_UNICHTOZHENIE_I_MY_NE_M");
    public static final ResKey MY_BY_HOTELI_NASHA_RASA_OCHEN_SLYS = new ResKey("MY_BY_HOTELI_NASHA_RASA_OCHEN_SLYS");
    public static final ResKey DLITEL_NAYA_PAUZA_ESLI_BY_NE_MIGAUSHII_IN = new ResKey("DLITEL_NAYA_PAUZA_ESLI_BY_NE_MIGAUSHII_IN");
    public static final ResKey NET_KONECHNO_MY_NICHEGO_NE_PLANIRUEM_I_DA = new ResKey("NET_KONECHNO_MY_NICHEGO_NE_PLANIRUEM_I_DA");
    public static final ResKey C_HEM_MY_MOZHEM_TEBE_POMOCH = new ResKey("C_HEM_MY_MOZHEM_TEBE_POMOCH");
    public static final ResKey PROSHU_PROSHENIYA_MY_NE_MOZHEM_POMOCH_S = new ResKey("PROSHU_PROSHENIYA_MY_NE_MOZHEM_POMOCH_S");
    public static final ResKey MY_PROSTO_HOTELI_TEBYA_PERENAPRAVIT_K_NAM = new ResKey("MY_PROSTO_HOTELI_TEBYA_PERENAPRAVIT_K_NAM");
    public static final ResKey ZAGOVORIL_VZVOLNOVANYM_GOLOSOM = new ResKey("ZAGOVORIL_VZVOLNOVANYM_GOLOSOM");
    @Override
    protected Map<Object, List> getQuestStateMap() {
        return [
                (QUEST_INIT_STATE)       :
                        [
                                SpasholeAssets.textResources.get(VY_PODLETAETE_K_PLANETE_N_VASH_IONNYI_SVY)
                                , QUEST_OPTION_1
                                , QUEST_OPTION_2
                                , QUEST_OPTION_3
                        ]
                , (STATE1)               :
                        [
                                SpasholeAssets.textResources.get(REZUL_TAT_SKANIROVANIYA_INFORMACIONNOGO_PO)
                                , QUEST_OPTION_2
                                , QUEST_OPTION_3
                        ]
                , (STATE2)               :
                        [
                                {
                                    text SpasholeAssets.textResources.get(NA_UDIVLENIE_SVYAZ_BYLA_USTANOVLENA_MGNO)

                                    enableSpeaker()
                                    text SpasholeAssets.textResources.get(ZDRAVSTVUI_MY_DAVNO_TEBYA_ZHDEM)

                                    option QUEST_OPTION_10
                                    option QUEST_OPTION_14
                                    option QUEST_OPTION_11
                                    option QUEST_OPTION_12
                                    option QUEST_OPTION_13
                                }
                        ]

                //
                , (STATE10)              :
                        [
                                SpasholeAssets.textResources.get(YA_PREDSTAVITEL_RASY_LUMEN_SAMOI_MIROLUB)
                                , QUEST_OPTION_20
                                , QUEST_OPTION_21
                                , QUEST_OPTION_22
                                , QUEST_OPTION_13
                                , {
                                    Scenario.LumenGlobals.noteLumensCatchMeInHyper()
                                }
                        ]
                , (STATE11)              :
                        [
                                SpasholeAssets.textResources.get(ETO_MY_PEREHVATILI_TEBYA_V_GIPER_PRYZHKE_I)
                                , QUEST_OPTION_20
                                , QUEST_OPTION_21
                                , QUEST_OPTION_22
                                , QUEST_OPTION_13
                                , {
                                    Scenario.LumenGlobals.noteLumensCatchMeInHyper()
                                }
                        ]
                , (STATE12)              :
                        [
                                {
                                    disableSpeaker()
                                    text SpasholeAssets.textResources.get(S_TOI_STORONY_TOZHE_TISHINA_ZATEM_ARTUR_S)

                                    enableSpeaker()
                                    text SpasholeAssets.textResources.get(PRIEM_ETO_PLANETA_LUMEN_MY_HOTIM_USTANOV)
                                }
                                , QUEST_OPTION_10
                                , QUEST_OPTION_14
                                , QUEST_OPTION_12
                                , QUEST_OPTION_13
                        ]

                //
                , (STATE20)              :
                        [
                                SpasholeAssets.textResources.get(NASHI_AGRESSIVNYE_SOSEDI_GROKI_NACHALI_A)
                                , QUEST_OPTION_34
                                , QUEST_OPTION_30
                                , QUEST_OPTION_31
                                , QUEST_OPTION_32
                                , QUEST_OPTION_33
                                , {
                                    Scenario.LumenGlobals.noteLumenPhys()
                                    Scenario.LumenGlobals.noteLumensHasProblemWithGroks()
                                }
                        ]
                , 21                     :
                        [
                                SpasholeAssets.textResources.get(NAM_GROZIT_POLNOE_UNICHTOZHENIE_I_MY_NE_M)
                                , QUEST_OPTION_23
                                , QUEST_OPTION_24
                                , QUEST_OPTION_22
                        ]

                , 23                     :
                        [
                                ARTUR_LAST_ARG
                                , QUEST_OPTION_13
                                , QUEST_OPTION_22
                                , QUEST_OPTION_25
                        ]

                , 25                     :
                        [
                                ARTUR_WE_HAVE_ENGINE_YOU_HELP_US
                                , QUEST_OPTION_62
                                , QUEST_OPTION_61
                                , QUEST_OPTION_70
                        ]

                //

                , (STATE30)              :
                        [
                                Scenario.LumenGlobals.ANSWER_ON_WHY_HAVE_SUCH_BIO
                                , QUEST_OPTION_31
                                , QUEST_OPTION_32
                                , QUEST_OPTION_33

                        ]
                , (STATE31)              :
                        [
                                SpasholeAssets.textResources.get(MY_BY_HOTELI_NASHA_RASA_OCHEN_SLYS)
                                , QUEST_OPTION_40
                                , QUEST_OPTION_41
                                , QUEST_OPTION_42
                                , QUEST_OPTION_13

                        ]
                , (STATE32)              :
                        [

                                {
                                    disableSpeaker()
                                    text SpasholeAssets.textResources.get(DLITEL_NAYA_PAUZA_ESLI_BY_NE_MIGAUSHII_IN)
                                    text speaker + SpasholeAssets.textResources.get(ZAGOVORIL_VZVOLNOVANYM_GOLOSOM)
                                    enableSpeaker()
                                    text SpasholeAssets.textResources.get(NET_KONECHNO_MY_NICHEGO_NE_PLANIRUEM_I_DA)
                                }
                                , QUEST_OPTION_40
                                , QUEST_OPTION_41
                                , QUEST_OPTION_42
                                , QUEST_OPTION_13
                        ]

                , 34                     :
                        [
                                Scenario.LumenGlobals.GROK_DESC
                                , QUEST_OPTION_31
                                , QUEST_OPTION_32
                                , QUEST_OPTION_33
                        ]

                , 40                     :
                        [
                                SpasholeAssets.textResources.get(C_HEM_MY_MOZHEM_TEBE_POMOCH)
                                , QUEST_OPTION_50
                                , QUEST_OPTION_51
                        ]
                , 41                     :
                        [
                                SpasholeAssets.textResources.get(C_HEM_MY_MOZHEM_TEBE_POMOCH)
                                , QUEST_OPTION_50
                                , QUEST_OPTION_51
                        ]
                , 42                     :
                        [
                                ARTUR_LAST_ARG
                                , QUEST_OPTION_101
                                , QUEST_OPTION_50
                                , QUEST_OPTION_51
                        ]

                , 50                     :
                        [
                                ARTUR_WE_HAVE_ENGINE_YOU_HELP_US
                                , QUEST_OPTION_60
                                , QUEST_OPTION_62
                                , QUEST_OPTION_61
                        ]
                , 51                     :
                        [
                                SpasholeAssets.textResources.get(PROSHU_PROSHENIYA_MY_NE_MOZHEM_POMOCH_S)
                                , QUEST_OPTION_60
                                , QUEST_OPTION_62
                                , QUEST_OPTION_61
                        ]

                , 60                     :
                        [
                                SpasholeAssets.textResources.get(MY_PROSTO_HOTELI_TEBYA_PERENAPRAVIT_K_NAM)
                                , QUEST_OPTION_61
                                , QUEST_OPTION_13
                                , QUEST_OPTION_70
                        ]

                , 62                     :
                        [

                                {
                                    disableSpeaker()
                                    text SpasholeAssets.textResources.get(SLYSHNO_HRIPLYI_VOZGLAS_STON_GULKII_ZVUK)

                                    speakerComponent.name = Scenario.LumenGlobals.MAIN_PLANET_SPEAKER_2
                                    setSpeaker(speakerComponent.name)
                                    enableSpeaker()
                                    text SpasholeAssets.textResources.get(C_HTO_TY_NADELAL)

                                    disableSpeaker()
                                    text SpasholeAssets.textResources.get(ZAMIGAL_SINII_INDIKATOR_PAUZY_POTOKA_N_C_HE)

                                    enableSpeaker()
                                    text SpasholeAssets.textResources.get(SPIKERU_STALO_PLOHO_YA_EGO_POMOSHNIK_DAV)
                                }
                                , QUEST_OPTION_61
                                , QUEST_OPTION_71
                        ]

                , 70                     :
                        [
                                {
                                    text SpasholeAssets.textResources.get(EM_U_NAS_NET_DVIGATELYA_YA_IZVINI)
                                    Scenario.LumenGlobals.noteGroksCouldHaveHyperEngine()

                                }
                                , QUEST_OPTION_80
                                , QUEST_OPTION_101
                                , QUEST_OPTION_71
                        ]

                //

                , (STATE61_Q_ACCEPTED)   :
                        [
                                {
                                    text SpasholeAssets.textResources.get(VOT_KOORDINATY_GLAVNOI_PLANETY_GROKOV_N, Scenario.GrokGlobals.MAIN_PLANET_POS)
                                    Scenario.LumenGlobals.acceptQuestHelpWithGroks()
                                }
                                , QUEST_OPTION_101
                                , QUEST_OPTION_EXIT
                        ]

                , 101                    :
                        [
                                {
                                    complete()
                                    def dialog = new LumenMainPlanetCallDialog(questWindow, planetUuid)
                                    dialog.state = dialog.PODROBNEE_STATE
                                    start(dialog)
                                }
                        ]

                //
                , (STATE3_CANCEL_CONTACT):
                        [

                                {
                                    disableSpeaker()
                                    text SpasholeAssets.textResources.get(VY_OTKLUCHILI_SVYAZ_ESHE_NEKOTOROE_VREMY)
                                    Scenario.LumenGlobals.firstContactCancelled = true
                                }
                                , QUEST_OPTION_EXIT

                        ]
        ]
    }

    public static final ResKey SLYSHNO_HRIPLYI_VOZGLAS_STON_GULKII_ZVUK = new ResKey("SLYSHNO_HRIPLYI_VOZGLAS_STON_GULKII_ZVUK");
    public static final ResKey C_HTO_TY_NADELAL = new ResKey("C_HTO_TY_NADELAL");
    public static final ResKey ZAMIGAL_SINII_INDIKATOR_PAUZY_POTOKA_N_C_HE = new ResKey("ZAMIGAL_SINII_INDIKATOR_PAUZY_POTOKA_N_C_HE");
    public static final ResKey SPIKERU_STALO_PLOHO_YA_EGO_POMOSHNIK_DAV = new ResKey("SPIKERU_STALO_PLOHO_YA_EGO_POMOSHNIK_DAV");
    public static final ResKey EM_U_NAS_NET_DVIGATELYA_YA_IZVINI = new ResKey("EM_U_NAS_NET_DVIGATELYA_YA_IZVINI");
    public static final ResKey VOT_KOORDINATY_GLAVNOI_PLANETY_GROKOV_N = new ResKey("VOT_KOORDINATY_GLAVNOI_PLANETY_GROKOV_N");
    public static final ResKey VY_OTKLUCHILI_SVYAZ_ESHE_NEKOTOROE_VREMY = new ResKey("VY_OTKLUCHILI_SVYAZ_ESHE_NEKOTOROE_VREMY");

}
