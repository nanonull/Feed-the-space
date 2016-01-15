package conversion7.spashole.game.artemis_odb.systems.gun;

import com.artemis.Component;

public class StationaryGunComponent extends Component {
    private float desiredWorldAngleRadians;

    public float getDesiredWorldAngleRads() {
        return desiredWorldAngleRadians;
    }


    public void setDesiredWorldAngleRadians(float desiredWorldAngleRadians) {
        this.desiredWorldAngleRadians = desiredWorldAngleRadians;
    }

    public void setup(float initialWorldAngle) {
        desiredWorldAngleRadians = initialWorldAngle;
    }

}
