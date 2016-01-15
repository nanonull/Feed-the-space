package conversion7.spashole.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.custom2d.TextureRegionColoredDrawable;
import conversion7.gdxg.core.custom2d.table.DefaultTable;
import conversion7.gdxg.core.custom2d.table.Panel;
import conversion7.gdxg.core.dialog.AbstractDialog;
import conversion7.spashole.game.ClientUi;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.Tags;
import conversion7.spashole.game.artemis_odb.systems.HealthComponent;
import conversion7.spashole.game.artemis_odb.systems.HealthSystem;
import conversion7.spashole.game.artemis_odb.systems.Position2dComponent;
import conversion7.spashole.game.artemis_odb.systems.Position2dSystem;
import conversion7.spashole.game.story.dialogs.PlayerActionsDialog;
import conversion7.spashole.game.utils.SpasholeUtils;

public class MainPanel extends Panel {


    Label coordsLabel;
    Label hpLabel;
    public MainPanel() {
        defaults().pad(ClientUi.SPACING);
        setBackground(new TextureRegionColoredDrawable(new Color(0, 0.24f, 0, 0.75f)
                , CommonAssets.pixelWhite));

        build(this);

        pack();
        setPosition(ClientUi.DOUBLE_SPACING,
                ClientUi.DOUBLE_SPACING);
    }

    private void build(Table table) {
        DefaultTable infoRow = new DefaultTable();
        table.add(infoRow);
        infoRow.add(new Label("Coordinates:", CommonAssets.uiSkin));
        coordsLabel = new Label("", CommonAssets.uiSkin);
        infoRow.add(coordsLabel).width(200);

        infoRow.row();

        infoRow.add(new Label("HP:", CommonAssets.uiSkin));
        hpLabel = new Label("", CommonAssets.uiSkin);
        infoRow.add(hpLabel);

        table.row();

        DefaultTable buttonsRow = new DefaultTable();
        table.add(buttonsRow);
        TextButton actionsBut = new TextButton("Actions", CommonAssets.uiSkin);
        buttonsRow.add(actionsBut);
        TextButton journalBut = new TextButton("Journal", CommonAssets.uiSkin);
        buttonsRow.add(journalBut);

        table.row();

        actionsBut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Entity entity = SpasholeApp.ARTEMIS_TAGS.getEntity(Tags.PLAYER_ENTITY);
                AbstractDialog.start(new PlayerActionsDialog(entity, SpasholeApp.ui.getDialogWindow()));
            }
        });

        journalBut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SpasholeApp.ui.getJournalWindow().show();
            }
        });

    }


    public void refresh(Entity entity) {
        Position2dComponent position2dComponent = Position2dSystem.components.getSafe(entity);
        if (position2dComponent == null) {
            coordsLabel.setText("unknown");
        } else {
            coordsLabel.setText(SpasholeUtils.coordsToString(position2dComponent.pos.x, position2dComponent.pos.y));
        }

        HealthComponent healthComponent = HealthSystem.components.getSafe(entity);
        if (healthComponent == null) {
            hpLabel.setText("unknown");
        } else {
            hpLabel.setText(healthComponent.health + "/" + healthComponent.maxHealth);
        }
    }
}
