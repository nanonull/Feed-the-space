package conversion7.spashole.game.artemis_odb.systems.ai;

import conversion7.gdxg.core.custom2d.ui_logger.UiLogger;
import conversion7.spashole.game.SpasholeApp;

public class AiManager {

    public static void switchState() {
        AiDynamicSystem aiDynamicSystem = SpasholeApp.ARTEMIS_ENGINE.getSystem(AiDynamicSystem.class);
        boolean switchTo = !aiDynamicSystem.isEnabled();
        aiDynamicSystem.setEnabled(switchTo);
        SpasholeApp.ARTEMIS_ENGINE.getSystem(AiStaticSystem.class).setEnabled(switchTo);
        UiLogger.addInfoLabel("AI enabled: " + switchTo);
    }

    public static void setAiState(boolean active) {
        SpasholeApp.ARTEMIS_ENGINE.getSystem(AiDynamicSystem.class).setEnabled(!active);
        AiManager.switchState();
    }
}
