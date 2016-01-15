package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.utils.SpasholeUtils;

public class InventoryItemComp extends Component {

    public Type type;

    public enum Type {

        WARP_ENGINE(SpasholeUtils.sceneToWorld(14f), SpasholeAssets.engine, 0.45f);

        private final float bodyRadius;
        private final TextureRegion textureRegion;
        private float imageScale;

        Type(float bodyRadius, TextureRegion textureRegion, float imageScale) {
            this.bodyRadius = bodyRadius;
            this.textureRegion = textureRegion;
            this.imageScale = imageScale;
        }

        public float getBodyRadius() {
            return bodyRadius;
        }

        public TextureRegion getTextureRegion() {
            return textureRegion;
        }

        public float getImageScale() {
            return imageScale;
        }
    }
}
