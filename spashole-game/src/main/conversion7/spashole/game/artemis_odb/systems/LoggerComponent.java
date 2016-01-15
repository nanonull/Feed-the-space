package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.concurrent.Callable;

public class LoggerComponent extends Component {
    public Callable<String> callable;

    public String getLogMessage() {
        String msg;
        try {
            msg = callable.call();
        } catch (Exception e) {
            throw new GdxRuntimeException(e);
        }
        return msg;
    }
}
