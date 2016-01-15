package conversion7.spashole.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.utils.Utils;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.Actor2dManager;
import conversion7.spashole.game.artemis_odb.systems.time.SchedulingSystem;

public class PointEffectHelper {

    public static void createPoint(Vector2 position, Color color) {
        Stage stage2d = SpasholeApp.core.getActiveStage2d();
        Image image = new Image(CommonAssets.pixelWhite);
        stage2d.addActor(image);
        image.setOrigin(Align.center);
        image.setScale(SpasholeUtils.sceneToWorld(2));
        image.setColor(color);
        Actor2dManager.adjustPositionCenterBySize(image);
        image.moveBy(position.x, position.y);
        SchedulingSystem.schedule(Utils.RANDOM.nextInt(3000) + 2000, image::remove);
    }
}
