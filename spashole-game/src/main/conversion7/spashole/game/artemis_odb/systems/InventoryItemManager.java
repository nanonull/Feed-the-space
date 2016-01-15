package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.stages.SolarSystemScene;

import java.util.UUID;

public class InventoryItemManager extends Manager {
    public static ComponentMapper<InventoryItemComp> components;

    public static int create(InventoryItemComp.Type itemType, float x, float y, Stage stage) {
        // artemis entity
        Entity item = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        UUID itemUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(item);
        components.create(item).type = itemType;
        Position2dSystem.components.create(item);
        ExplosiveManager.components.create(item);
        HealthSystem.components.create(item).setup(SolarSystemScene.INV_ITEM_HP);
        NameManager.components.create(item).name = itemType.toString();

        // box2d
        Body body = SpasholeApp.box2dWorld.createBody(Box2dBodySystem.INF_INERTIA_BODY_DEF);
        body.setUserData(itemUuid);
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(item);
        box2dBodyComponent.body = body;
        Shape shape = new CircleShape();
        shape.setRadius(itemType.getBodyRadius());
        body.createFixture(shape, 1);
        body.setTransform(x, y, 0);

        // visual
        Group group = new Group();
        stage.addActor(group);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(item);
        actor2dComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeApp.WORLD_SCALE);
        group.setZIndex(0);

        Image mainImage = new Image(itemType.getTextureRegion());
        group.addActor(mainImage);
        mainImage.setOrigin(Align.center);
        mainImage.setScale(itemType.getImageScale());
        Actor2dManager.adjustPositionCenterBySize(mainImage);

        return item.getId();
    }
}