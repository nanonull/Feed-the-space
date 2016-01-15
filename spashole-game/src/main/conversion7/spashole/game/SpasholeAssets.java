package conversion7.spashole.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import conversion7.gdxg.core.CommonAssets;
import conversion7.gdxg.core.strings.CustomI18NBundle;

public class SpasholeAssets extends CommonAssets {

    public static CustomI18NBundle<ResKey> textResources;
    public static String track1;
    public static String track2Landing;
    public static String ritualAudio;
    public static String warpAudio;

    public static TextureRegion glow;
    public static TextureRegion shipBlue;
    public static TextureRegion shipRed;
    public static TextureRegion shipBroken;
    public static TextureRegion meteor;
    public static TextureRegion planet1;
    public static TextureRegion planet2;
    public static TextureRegion sun2;
    public static TextureRegion explosion1;
    public static TextureRegion explosion3;
    public static TextureRegion explosion6;
    public static TextureRegion explosion8;
    public static TextureRegion explosion12;
    public static TextureRegion gun;
    public static TextureRegion player_schematic;
    public static TextureRegion frog;
    public static TextureRegion house;
    public static TextureRegion flag;
    public static TextureRegion engine;
    public static TiledDrawable space1;
    public static Texture warpImg;
    public static Texture blackholeImg;

    public static void loadAll() {
        loadDefaultSkin();
        loadManagedAssets();
        loadDefaultGraphic();

        prepareGameAudio();
        loadGameTextures();
        registerFonts();
        textResources = new CustomI18NBundle<>(STRINGS_FOLDER + "/text");
    }

    protected static void loadGameTextures() {
        glow = new TextureRegion(imagesAtlas.findRegion("glow"));
        shipBlue = new TextureRegion(imagesAtlas.findRegion("ship_blue"));
        shipRed = new TextureRegion(imagesAtlas.findRegion("ship_red"));
        shipBroken = new TextureRegion(imagesAtlas.findRegion("ship_broken"));
        meteor = new TextureRegion(imagesAtlas.findRegion("meteor"));
        planet1 = new TextureRegion(imagesAtlas.findRegion("planet1"));
        planet2 = new TextureRegion(imagesAtlas.findRegion("planet2"));
        sun2 = new TextureRegion(imagesAtlas.findRegion("sun2"));
        explosion1 = new TextureRegion(imagesAtlas.findRegion("explosion1"));
        explosion3 = new TextureRegion(imagesAtlas.findRegion("explosion3"));
        explosion6 = new TextureRegion(imagesAtlas.findRegion("explosion6"));
        explosion8 = new TextureRegion(imagesAtlas.findRegion("explosion8"));
        explosion12 = new TextureRegion(imagesAtlas.findRegion("explosion12"));
        gun = new TextureRegion(imagesAtlas.findRegion("gun"));
        player_schematic = new TextureRegion(imagesAtlas.findRegion("player_schematic"));
        frog = new TextureRegion(imagesAtlas.findRegion("frog"));
        house = new TextureRegion(imagesAtlas.findRegion("house"));
        flag = new TextureRegion(imagesAtlas.findRegion("flag"));
        TextureRegion spaceTexReg = new TextureRegion(imagesAtlas.findRegion(SPACE_PARALLAX + "space1"));
        space1 = new TiledDrawable(spaceTexReg);
        engine = new TextureRegion(imagesAtlas.findRegion("engine"));

        warpImg = new Texture(Gdx.files.internal(IMAGES_FOLDER + "warp.png"));
        blackholeImg = new Texture(Gdx.files.internal(IMAGES_FOLDER + "bhole2.png"));
    }

    private static void prepareGameAudio() {
        track1 = AUDIO_FOLDER + "track1.mp3";
        track2Landing = AUDIO_FOLDER + "track2_landing.mp3";
        ritualAudio = AUDIO_FOLDER + "ritual.mp3";
        warpAudio = AUDIO_FOLDER + "warp.mp3";
    }

}
