package conversion7.spashole.game.story;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.custom2d.ui_logger.UiLogger;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.ui.JournalWindow;

public class Journal {

    private static Array<String> entries = new Array<>();

    public static void addToJournal(String line) {
        if (entries.contains(line, false)) {
            return;
        }
        Label label = new Label(line, CommonAssets.labelStyle14orange);
        label.setWrap(true);

        JournalWindow journalWindow = SpasholeApp.ui.getJournalWindow();
        journalWindow.getEntriesTable().add(label).expandX().fill();
        journalWindow.getEntriesTable().row();

        UiLogger.addInfoLabel("Added new entry to the Journal");
    }
}
