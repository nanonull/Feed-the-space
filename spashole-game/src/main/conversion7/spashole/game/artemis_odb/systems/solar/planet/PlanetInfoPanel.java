package conversion7.spashole.game.artemis_odb.systems.solar.planet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.custom2d.TextureRegionColoredDrawable;
import conversion7.gdxg.core.custom2d.table.DefaultTable;
import conversion7.gdxg.core.custom2d.table.Panel;
import conversion7.spashole.game.ClientUi;

public class PlanetInfoPanel extends Panel {

    Label mainLabel;

    public PlanetInfoPanel(int planetId) {
        defaults().pad(ClientUi.SPACING);
        setBackground(new TextureRegionColoredDrawable(new Color(0, 0.24f, 0, 0.75f)
                , CommonAssets.pixelWhite));

        build(this);
    }

    private void build(Table mainTable) {
        DefaultTable infoRow = new DefaultTable();
        mainTable.add(infoRow);
        mainLabel = new Label("", CommonAssets.uiSkin);
        infoRow.add(mainLabel);
    }

    public void refresh(String name, int position) {
        mainLabel.setText(name + " [" + position + "]");
        pack();
    }
}
