package conversion7.spashole.tests.steps
import com.badlogic.gdx.Gdx
import com.jayway.awaitility.Awaitility
import conversion7.gdxg.core.BaseApp
import conversion7.gdxg.core.CommonApp
import conversion7.gdxg.core.artemis_odb.CoreThreadSystem
import conversion7.gdxg.core.utils.Utils
import conversion7.spashole.game.ClientApplication
import conversion7.spashole.game.ClientCore
import conversion7.spashole.game.DebugFlags
import conversion7.spashole.tests.system.TestApp
import conversion7.spashole.tests.system.TestableClientCore

public class CoreSteps {


    private final static String PAUSE_CLIENT_CORE_AT = "PAUSE_CLIENT_CORE_AT";

    static ClientCore clientCore;

    ClientCore getClientCore() {
        return createClientCore()
    }

    ClientCore createClientCore() {
        if (clientCore == null) {
            // use properties from game
            File testsRootFile = new File("").getAbsoluteFile();
            File rootProjectFile = testsRootFile.getParentFile();
            BaseApp.root = new File(rootProjectFile, "spashole-game").getCanonicalFile();

            DebugFlags.DIALOGS_ENABLED = false
            clientCore = new TestableClientCore();
            ClientApplication.start(clientCore);
            clientCore.waitCoreCreated();
        }
        return clientCore;
    }

    void pause() {
        clientCore.pauseRender()
        def coreThreadSystem = CommonApp.ARTEMIS_ENGINE.getSystem(CoreThreadSystem.class)
        coreThreadSystem.setPaused(true);
        Awaitility.await().until({
            coreThreadSystem.resumeWaitingStarted
        })
        TestApp.SESSION.put(PAUSE_CLIENT_CORE_AT, Gdx.graphics.getFrameId())
    }

    void resume() {
        clientCore.resumeRender()
        def coreThreadSystem = CommonApp.ARTEMIS_ENGINE.getSystem(CoreThreadSystem.class)
        Awaitility.await().until({
            !coreThreadSystem.resumeWaitingStarted
        })
    }

    void assertClientCoreRenderIsNotInvoked() {
        Utils.sleepThread(2000);
        assert TestApp.SESSION.get(PAUSE_CLIENT_CORE_AT) == Gdx.graphics.getFrameId()
    }

    void assertClientCoreRenderIsInProgress() {
        Awaitility.await().until({
            TestApp.SESSION.get(PAUSE_CLIENT_CORE_AT) < Gdx.graphics.getFrameId()
        })
    }

    void waitForNextRenderFrame() {
        def frameId = Gdx.graphics.getFrameId()
        Awaitility.await().until({
            frameId < Gdx.graphics.getFrameId()
        })
    }
}
