package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import conversion7.spashole.game.story.SpasholeDialog;

import java.util.concurrent.Callable;

public class EntityDialogComponent extends Component {
    /** Return null is no dialog */
    public Callable<SpasholeDialog> dialogBuilder;
}
