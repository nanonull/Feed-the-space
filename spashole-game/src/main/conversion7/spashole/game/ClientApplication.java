package conversion7.spashole.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import conversion7.gdxg.core.AbstractClientCore;
import conversion7.gdxg.core.CommonApp;
import conversion7.gdxg.core.utils.Utils;
import conversion7.gdxg.core.utils.WaitLibrary;
import org.slf4j.Logger;

import java.io.File;

public class ClientApplication {

    private static final Logger LOG = Utils.getLoggerForClass();

    public static void main(String[] args) {
        start(new ClientCore());
    }

    public static void start(AbstractClientCore application) {
        LOG.info("Application start has been triggered: " + application.getClass().getSimpleName());
        LOG.info("applicationRoot: " + new File("").getAbsolutePath());

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Feed the space";
        cfg.vSyncEnabled = true;
        cfg.width = CommonApp.commonProperties.getInteger("SCREEN_WIDTH_IN_PX");
        cfg.height = CommonApp.commonProperties.getInteger("SCREEN_HEIGHT_IN_PX");
        cfg.resizable = false;
        cfg.fullscreen = CommonApp.commonProperties.getInteger("FULLSCREEN") == 1;

        new LwjglApplication(application, cfg);

        Utils.createThreadExceptionHandler(application);

        WaitLibrary.waitClientCoreInitialized(application);
        LOG.info("Application started.");
    }

}
