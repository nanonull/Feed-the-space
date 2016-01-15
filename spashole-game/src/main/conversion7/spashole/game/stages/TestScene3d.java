package conversion7.spashole.game.stages;

import com.badlogic.gdx.graphics.Color;
import conversion7.gdxg.core.CommonApp;
import conversion7.gdxg.core.custom3dscene.ModelActor;
import conversion7.gdxg.core.game_scene.GameScene3d;
import conversion7.gdxg.core.utils.geometry.Modeler;

public class TestScene3d extends GameScene3d {

    @Override
    public void init() {
        super.init();
        ModelActor modelActor = new ModelActor(
                Modeler.buildBox(Color.GREEN, 1), CommonApp.modelBatch);
        stage3d.addNode(modelActor);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void fadeOut(Color toColor, int duration) {

    }

    @Override
    public void fadeIn(int durationMs) {

    }
}
