package conversion7.spashole.game.artemis_odb.systems;

import conversion7.spashole.game.SpasholeApp;

public class MovementConstant {

    public static final MovementConstant SHIP_PARAMS = new MovementConstant(
            36000000 * SpasholeApp.WORLD_SCALE_PI_QUAD
            , 72000000 * SpasholeApp.WORLD_SCALE_PI_QUAD
    );
    public static final MovementConstant HUMAN_PARAMS = new MovementConstant(
            1800 * SpasholeApp.WORLD_SCALE
            , 1800 * SpasholeApp.WORLD_SCALE
    );
    public static final MovementConstant ANIMAL_PARAMS = new MovementConstant(
            360000 * SpasholeApp.WORLD_SCALE
            , 720000 * SpasholeApp.WORLD_SCALE
    );

    /** Affected by delta */
    public final float moveMltpl;
    public final float torqueForce;
    public final float evasionMlt;

    public MovementConstant(float moveMltpl, float torqueForce) {
        this.moveMltpl = moveMltpl;
        this.torqueForce = torqueForce;
        this.evasionMlt = moveMltpl * 20;
    }

    public float getMoveMltpl() {
        return moveMltpl;
    }

    public float getTorqueForce() {
        return torqueForce;
    }

}
