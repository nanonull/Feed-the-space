package conversion7.spashole.game;

import conversion7.gdxg.core.AbstractClientUi;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.custom2d.AnimatedWindow;
import conversion7.spashole.game.ui.CentralPanel;
import conversion7.spashole.game.ui.FinalPanel;
import conversion7.spashole.game.ui.JournalWindow;
import conversion7.spashole.game.ui.MainPanel;

public class ClientUi extends AbstractClientUi {
    private JournalWindow journalWindow;
    private MainPanel playerStatePanel;
    private FinalPanel finalPanel;
    private CentralPanel centralPanel;

    public ClientUi() {
        super();
        playerStatePanel = new MainPanel();
        stageGUI.addActor(playerStatePanel);
        journalWindow = new JournalWindow(stageGUI, "Journal", CommonAssets.uiSkin, AnimatedWindow.Direction.right);
    }

    public JournalWindow getJournalWindow() {
        return journalWindow;
    }

    public MainPanel getPlayerStatePanel() {
        return playerStatePanel;
    }

    public FinalPanel getFinalPanel() {
        if (finalPanel == null) {
            finalPanel = new FinalPanel();
            stageGUI.addActor(finalPanel);
        }
        return finalPanel;
    }

    public CentralPanel getCentralPanel() {
        if (centralPanel == null) {
            centralPanel = new CentralPanel();
            stageGUI.addActor(centralPanel);
        }
        return centralPanel;
    }

    public void hideGameWindows() {
        journalWindow.hide();
        playerStatePanel.hide();
        if (centralPanel != null) {
            centralPanel.hide();
        }
    }

    public void hide() {
        stageGUI.getRoot().setVisible(false);
    }

    public void show() {
        stageGUI.getRoot().setVisible(true);
    }
}
