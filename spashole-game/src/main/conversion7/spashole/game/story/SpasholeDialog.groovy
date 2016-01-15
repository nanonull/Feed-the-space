package conversion7.spashole.game.story

import conversion7.gdxg.core.dialog.AbstractDialog
import conversion7.gdxg.core.dialog.QuestOption
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.ResKey
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.SpasholeAssets
import conversion7.spashole.game.artemis_odb.systems.CameraControllerFocusSystem

public abstract class SpasholeDialog extends AbstractDialog {

    QuestOption QUEST_OPTION_RETURN = new QuestOption(
            SpasholeAssets.textResources.get(new ResKey('QUEST_OPTION_RETURN'))
            , {
        returnBack()
    })
    final QuestOption QUEST_OPTION_EXIT = new QuestOption(
            SpasholeAssets.textResources.get(new ResKey('QUEST_OPTION_EXIT'))
            , {
        newState(QUEST_COMPLETED_STATE)
    });

    protected UUID cameraFocusTargetEntityUuid;

    public SpasholeDialog(DialogWindow questWindow) {
        super(questWindow);
        cameraFocusTargetEntityUuid = CameraControllerFocusSystem.getTargetEntityUuid();
        if (cameraFocusTargetEntityUuid != null) {
            focusOn(cameraFocusTargetEntityUuid)
        }
    }

    @Override
    protected void initQuestState() {
        SpasholeApp.core.setWorldPause(true)
        def name = initSpeakerName()
        if (name == null) {
            disableSpeaker()
        } else {
            setSpeaker(name)
            enableSpeaker()
        }
    }

    /**Return null if no speaker*/
    protected abstract String initSpeakerName();

    @Override
    public void setPictureViewActive(boolean active) {
        super.setPictureViewActive(active);
        if (active) {
            CameraControllerFocusSystem.setMode(CameraControllerFocusSystem.Mode.FULL_SCREEN);
        } else {
            CameraControllerFocusSystem.setMode(CameraControllerFocusSystem.Mode.QUEST_VIEW);
        }
    }

    @Override
    public void complete() {
        super.complete();
        CameraControllerFocusSystem.setMode(CameraControllerFocusSystem.Mode.FULL_SCREEN);
        CameraControllerFocusSystem.setTarget(cameraFocusTargetEntityUuid);
        questWindow.setPictureViewEnabled(true);
        SpasholeApp.core.setWorldPause(false)
    }

    public static void focusOn(UUID entityUuid) {
        CameraControllerFocusSystem.setTarget(entityUuid, 0);
        CameraControllerFocusSystem.setMode(CameraControllerFocusSystem.Mode.QUEST_VIEW);
    }
}
