package conversion7.spashole.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import conversion7.gdxg.core.AbstractClientUi;
import conversion7.gdxg.core.CommonConstants;
import conversion7.gdxg.core.custom2d.AnimatedWindow;
import conversion7.gdxg.core.custom2d.table.TableWithScrollPane;

public class JournalWindow extends AnimatedWindow {

    private TableWithScrollPane entriesTable;

    public JournalWindow(Stage stage, String title, Skin skin, int direction) {
        super(stage, title, skin, direction);
        addCloseButton();
        buildBody(this);
    }

    public TableWithScrollPane getEntriesTable() {
        return entriesTable;
    }

    private void buildBody(Table mainTable) {
        entriesTable = new TableWithScrollPane();
        entriesTable.defaults().pad(AbstractClientUi.SPACING);
        ScrollPane scrollPane = entriesTable.getScrollPane();
        mainTable.add(scrollPane).expand().fill().pad(AbstractClientUi.SPACING);
        scrollPane.setScrollingDisabled(false, false);
    }

    @Override
    public void onShow() {
        setBounds(0, 0
                , CommonConstants.SCREEN_WIDTH_IN_PX
                , CommonConstants.SCREEN_HEIGHT_IN_PX);
        updateAnimationBounds();
    }

}
