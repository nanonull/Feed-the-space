package conversion7.spashole.game.story.dialogs.planet

import com.artemis.Entity
import conversion7.gdxg.core.dialog.view.DialogWindow
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetManager
import conversion7.spashole.game.artemis_odb.systems.solar.planet.PlanetSpeakerComponent
import conversion7.spashole.game.story.SpasholeDialog

abstract class CommonPlanetCallDialog extends SpasholeDialog {

    UUID planetUuid
    Entity planetEntity
    PlanetSpeakerComponent speakerComponent

    CommonPlanetCallDialog(DialogWindow questWindow, UUID planetUuid) {
        super(questWindow)
        this.planetUuid = planetUuid
        planetEntity = SpasholeApp.ARTEMIS_UUIDS.getEntity(planetUuid)
        speakerComponent = PlanetManager.getPlanetSpeaker(planetEntity.id)
    }

    @Override
    protected String initSpeakerName() {
        return speakerComponent.name
    }
}
