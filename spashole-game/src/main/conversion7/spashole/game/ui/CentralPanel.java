package conversion7.spashole.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.CommonConstants;
import conversion7.gdxg.core.custom2d.TextureRegionColoredDrawable;
import conversion7.gdxg.core.custom2d.table.DefaultTable;
import conversion7.gdxg.core.custom2d.table.Panel;
import conversion7.spashole.game.ClientUi;

public class CentralPanel extends Panel {

    Label mainLabel;

    public CentralPanel() {
        defaults().pad(ClientUi.SPACING);
        setBackground(new TextureRegionColoredDrawable(new Color(0.5f, 0.5f, 0.5f, 0.5f)
                , CommonAssets.pixelWhite));
        build(this);
        hide();

    }

    private void build(Table mainTable) {
        DefaultTable infoRow = new DefaultTable();
        mainTable.add(infoRow)
                .fill().expand();
        mainLabel = new Label("", CommonAssets.uiSkin);
        infoRow.add(mainLabel).width(CommonConstants.SCREEN_1_3_WIDTH_PX);
        mainLabel.setWrap(true);
    }

    public void refresh(String text) {
        mainLabel.setText(text);
        pack();
        setPosition(CommonConstants.SCREEN_HALF_WIDTH_IN_PX - getWidth() / 2
                , CommonConstants.SCREEN_1_3_HEIGHT_PX);
    }
}
