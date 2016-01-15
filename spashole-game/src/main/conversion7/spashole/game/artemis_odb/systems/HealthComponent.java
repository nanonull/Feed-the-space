package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;

public class HealthComponent extends Component {
    public int health = 1;
    public int maxHealth = 1;
    public Runnable onDeathTrigger;

    public float getHealthPercent() {
        return health / (float) maxHealth;
    }

    public void setup(int hp) {
        maxHealth = hp;
        health = hp;
    }
}
