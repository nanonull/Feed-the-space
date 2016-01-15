package conversion7.spashole.game.story.dialogs.planet

import conversion7.gdxg.core.dialog.view.DialogWindow

class LumenCommonPlanetCallDialog extends LumenBasePlanetCallDialog{
    LumenCommonPlanetCallDialog(DialogWindow questWindow, UUID planetUuid) {
        super(questWindow, planetUuid)
        state = COMMON_PLANET_STATE
    }
}
