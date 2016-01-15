package conversion7.spashole.game.artemis_odb.systems.utils;

public class ShotRunnable {

    public boolean singleShot;
    public Runnable runnable;

    public ShotRunnable(boolean singleShot, Runnable runnable){
        this.singleShot = singleShot;
        this.runnable = runnable;
    }

}
