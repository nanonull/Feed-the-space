package conversion7.spashole.game.artemis_odb.systems.gun;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class GunComponent extends Component {
    public int damage = 1;
    public boolean fireWhenBulletReady;
    public float barrelDistance;
    public float bulletReady = 0;
    public Color bulletColor = Color.WHITE;
    public float bulletReloadTime = 0.2f;

    public void makeShot() {
        fireWhenBulletReady = true;
    }
}
