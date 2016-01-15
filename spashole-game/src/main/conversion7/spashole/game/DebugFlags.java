package conversion7.spashole.game;

import conversion7.gdxg.core.dialog.AbstractDialog;

/**
 * All flags should be true or removed in release.
 */
public class DebugFlags {

    public static final boolean DEBUG_MODE_INACTIVE = true;

    public static final boolean ALL_SYSTEM_ACTIVE = true;
    public static final boolean AI_ENABLED = true;
    public static final boolean BOX2D_DEBUG_RENDER_DISABLED = true;

    public static boolean DIALOGS_ENABLED = true;

    public static final boolean CREATE_LUMEN_PLANETS = true;
    public static final boolean CREATE_ALL_LUMEN_PLANETS = true;
    public static final boolean PLANET_NORMAL_ROTATION_SPEED = true;
    public static final boolean PLANET_NORMAL_MOVE_SPEED = true;
    public static final boolean PLANET_ROTATION_ENABLED = true;
    public static final boolean NORM_ANGLE_BTW_PLANET_BODIES = true;
    public static final boolean PLANET_BASE_RND_POS = true;
    public static final boolean DONT_CREATE_STATIC_GUNS_ON_START = true;
    public static final boolean NORM_PLANET_CONSTR_SPEED = true;

    public static final boolean PLAYER_SHIP_NORM_HP = true;
    public static final boolean AI_SHIP_NORM_HP = true;

    public static final boolean SPAWN_SHIPS = true;
    public static final boolean SPAWN_ANIMAL = true;
    public static final boolean NORM_SPAWN_SHIPS_LIMIT = true;
    public static final boolean CAMERA_CNTRL_ENABLED = true;
    public static final boolean LUMEN_MAIN_PLANET_NOT_CAPTURED = true;
    public static final boolean DO_NOT_SHOW_WALK_STEPS = true;
    public static final boolean DONT_CREATE_INVENT_ITEM_NEAR_PLAYER = true;
    public static final boolean RANDOM_WARP_ENGINE = true;
    public static final boolean GROK_ALIVE_BY_SCENARIO = true;
    public static final boolean NORM_FINAL_TIMES = true;

    public static void init() {
        AbstractDialog.setDialogsEnabled(DIALOGS_ENABLED);
    }
}
