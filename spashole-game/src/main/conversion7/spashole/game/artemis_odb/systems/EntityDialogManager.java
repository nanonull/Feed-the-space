package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import conversion7.gdxg.core.custom2d.ui_logger.UiLogger;
import conversion7.spashole.game.story.SpasholeDialog;
import conversion7.spashole.game.utils.SpasholeUtils;

public class EntityDialogManager extends Manager {
    public static ComponentMapper<EntityDialogComponent> components;

    public static SpasholeDialog getDialog(Entity entity) {
        if (EntityDialogManager.components.has(entity)) {
            EntityDialogComponent dialogComponent = EntityDialogManager.components.get(entity);
            if (dialogComponent.dialogBuilder != null) {
                SpasholeDialog spasholeQuest;
                try {
                    spasholeQuest = dialogComponent.dialogBuilder.call();
                    return spasholeQuest;
                } catch (Exception e) {
                    UiLogger.addErrorLabel(e.getMessage());
                    SpasholeUtils.LOG.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }
}