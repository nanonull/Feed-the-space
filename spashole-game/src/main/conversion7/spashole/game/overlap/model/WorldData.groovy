package conversion7.spashole.game.overlap.model

import conversion7.spashole.game.overlap.SceneLoader
import conversion7.spashole.game.utils.SpasholeUtils

/** Converted from Overlap scene data */
class WorldData {
    public String objectCode
    public float sceneX
    public float worldX
    public float sceneY
    public float worldY
    public HashMap<String, String> vars = new HashMap<>()

    void parseSceneObject(OverlapSceneData.Composite.SLabel sceneObject) {
        objectCode = sceneObject.text;
        SceneLoader.loadArgs(sceneObject.customVars, vars);
        sceneX = sceneObject.x * SceneLoader.OVERLAP_TO_SCENE + sceneObject.width / 2;
        worldX = SpasholeUtils.sceneToWorld(sceneX);
        sceneY = sceneObject.y * SceneLoader.OVERLAP_TO_SCENE + sceneObject.height / 2;
        worldY = SpasholeUtils.sceneToWorld(sceneY);
    }
}
