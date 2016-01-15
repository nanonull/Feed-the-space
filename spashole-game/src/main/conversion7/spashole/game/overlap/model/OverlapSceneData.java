package conversion7.spashole.game.overlap.model;

public final class OverlapSceneData {

    public final String sceneName;
    public final Composite composite;
    public final PhysicsPropertiesVO physicsPropertiesVO;

    public OverlapSceneData(String sceneName, Composite composite, PhysicsPropertiesVO physicsPropertiesVO) {
        this.sceneName = sceneName;
        this.composite = composite;
        this.physicsPropertiesVO = physicsPropertiesVO;
    }

    public static final class Composite {
        public final SImage sImages[];
        public SLabel sLabels[];
        public final Layer layers[];

        public Composite(SImage[] sImages, Layer[] layers) {
            this.sImages = sImages;
            this.layers = layers;
        }

        public static final class SImage {
            public final long uniqueId;
            public final String[] tags;
            public final float x;
            public final float y;
            public final float originX;
            public final float originY;
            public String customVars;
            public final String layerName;
            public final String imageName;
            public final Boolean isRepeat;
            public final Long zIndex;

            public SImage(long uniqueId, String[] tags, float x, float y, float originX, float originY, String layerName, String imageName, Boolean isRepeat, Long zIndex) {
                this.uniqueId = uniqueId;
                this.tags = tags;
                this.x = x;
                this.y = y;
                this.originX = originX;
                this.originY = originY;
                this.layerName = layerName;
                this.imageName = imageName;
                this.isRepeat = isRepeat;
                this.zIndex = zIndex;
            }
        }

        public static final class SLabel {
            public String customVars;
            public final long uniqueId;
            public final String[] tags;
            public final long x;
            public final long y;
            public final long originX;
            public final long originY;
            public final long zIndex;
            public final String layerName;
            public final String text;
            public final String style;
            public final long size;
            public final long align;
            public final float width;
            public final float height;

            public SLabel(long uniqueId, String[] tags, long x, long y, long originX, long originY, long zIndex, String layerName, String text, String style, long size, long align, float width, float height) {
                this.uniqueId = uniqueId;
                this.tags = tags;
                this.x = x;
                this.y = y;
                this.originX = originX;
                this.originY = originY;
                this.zIndex = zIndex;
                this.layerName = layerName;
                this.text = text;
                this.style = style;
                this.size = size;
                this.align = align;
                this.width = width;
                this.height = height;
            }
        }

        public static final class Layer {
            public final String layerName;
            public final boolean isVisible;

            public Layer(String layerName, boolean isVisible) {
                this.layerName = layerName;
                this.isVisible = isVisible;
            }
        }
    }

    public static final class PhysicsPropertiesVO {

        public PhysicsPropertiesVO() {
        }
    }
}