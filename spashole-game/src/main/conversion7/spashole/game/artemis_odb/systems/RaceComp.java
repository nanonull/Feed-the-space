package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class RaceComp extends Component {
    public Race race = Race.UNKNOWN;

    public enum Race {
        LUMEN(Color.YELLOW),
        GROK(Color.PURPLE),
        HUMAN(Color.BLUE),
        ANIMAL_NEUT(Color.GREEN),
        ANIMAL_AGGR(Color.RED),
        UNKNOWN(Color.GRAY);

        private Color color;

        Race(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}
