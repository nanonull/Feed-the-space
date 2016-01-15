package conversion7.spashole.tests.system;

import com.artemis.WorldConfigurationBuilder;
import conversion7.spashole.game.ClientCore;
import conversion7.spashole.tests.artemis_odb.TestSystem;

public class TestableClientCore extends ClientCore {

    @Override
    protected WorldConfigurationBuilder getArtemisOdbConfigBuilder() {
        return super.getArtemisOdbConfigBuilder().with(
                WorldConfigurationBuilder.Priority.HIGH, new TestSystem()
        );
    }
}
