package conversion7.spashole.game.overlap;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import conversion7.gdxg.core.dialog.AbstractDialog;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.Tags;
import conversion7.spashole.game.artemis_odb.systems.ActivationTriggerComponent;
import conversion7.spashole.game.artemis_odb.systems.ActivationTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.EntityEntersCameraViewTriggerComponent;
import conversion7.spashole.game.artemis_odb.systems.EntityEntersCameraViewTriggerManager;
import conversion7.spashole.game.artemis_odb.systems.EntityInventoryManager;
import conversion7.spashole.game.artemis_odb.systems.PlayerInputSystem;
import conversion7.spashole.game.artemis_odb.systems.ShipComponent;
import conversion7.spashole.game.artemis_odb.systems.ShipManager;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.story.Scenario;
import conversion7.spashole.game.story.dialogs.BrokenShipDialog;
import conversion7.spashole.game.story.dialogs.BrokenShipEnterDialog;
import conversion7.spashole.game.utils.SpasholeUtils;

public class SceneLoaderTags {

    public static void handleTags(String[] tags, int entityId) {
        for (String tag : tags) {
            if (tag.equals("player")) {
                PlayerInputSystem.setTarget(entityId);
                SpasholeApp.ARTEMIS_TAGS.register(Tags.PLAYER_SHIP, entityId);
                ShipComponent shipComponent = ShipManager.components.get(entityId);
                shipComponent.pilotable = true;
                EntityInventoryManager.components.create(entityId);
            } else if (tag.equals(Tags.Q_BROKEN_SHIP)) {
                Scenario.qBrokenShip = SpasholeApp.ARTEMIS_UUIDS.getUuid(
                        SpasholeApp.ARTEMIS_ENGINE.getEntity(entityId));
                ShipComponent shipComponent = ShipManager.components.get(entityId);
                shipComponent.pilotable = true;
                shipComponent.engineWorks = false;
                EntityEntersCameraViewTriggerComponent entityEntersCameraViewTriggerComponent =
                        EntityEntersCameraViewTriggerManager.componentCreate(entityId);
                entityEntersCameraViewTriggerComponent.addRunnable(true, "BrokenShipDialog", () -> {
                    AbstractDialog.start(new BrokenShipDialog(entityId, SpasholeApp.ui.getDialogWindow()));
                });

                ActivationTriggerComponent activationTriggerComponent = ActivationTriggerManager.components.create(entityId);
                activationTriggerComponent.triggerAndRemove = false;
                activationTriggerComponent.runnable = () -> {
                    PlayerInputSystem.doNotExecuteCommonActivation = true;
                    AbstractDialog.start(new BrokenShipEnterDialog(
                            activationTriggerComponent.activatedBy,
                            Scenario.qBrokenShip,
                            SpasholeApp.ui.getDialogWindow()));
                };

                GunSystem.components.remove(entityId);
                shipComponent.shipImg.setDrawable(new TextureRegionDrawable(SpasholeAssets.shipBroken));
                shipComponent.gunImage.remove();

            } else {
                SpasholeUtils.logErrorWithCurrentStacktrace("Unknown tag: " + tag);
            }
        }
    }
}
