package conversion7.spashole.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import conversion7.gdxg.core.CommonApp;

public class SpasholeApp extends CommonApp {
    public static final float WORLD_SCALE = 0.1f;
    public static final float WORLD_SCALE_PI_QUAD = WORLD_SCALE * WORLD_SCALE * MathUtils.PI;

    public static ClientCore core;
    public static ClientGraphic graphic;
    public static ClientUi ui;
    public static World box2dWorld;
    public static Box2DDebugRenderer box2dWorldDebugRenderer;
}
