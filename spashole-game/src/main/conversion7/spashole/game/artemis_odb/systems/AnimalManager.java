package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.SpasholeAssets;
import conversion7.spashole.game.artemis_odb.systems.ai.AiDynamicSystem;
import conversion7.spashole.game.artemis_odb.systems.ai.helpers.AiFindEnemyHelper;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunComponent;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.stages.SolarSystemScene;
import conversion7.spashole.game.utils.SpasholeUtils;

import java.util.UUID;

public class AnimalManager extends Manager {
    public static ComponentMapper<AnimalComponent> components;
    private static float RADIUS = 2f;
    private static float BARREL_DST = RADIUS + SpasholeUtils.sceneToWorld(6);

    public static Entity create(float worldX, float worldY, RaceComp.Race race, Stage stage) {
        // artemis entity
        Entity animal = SpasholeApp.ARTEMIS_ENGINE.createEntity();
        UUID shipUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(animal);
        components.create(animal);
        Position2dSystem.components.create(animal);
        Rotation2dSystem.components.create(animal);
        HealthSystem.components.create(animal).setup(SolarSystemScene.ANIMAL_HP);
        NameManager.components.create(animal).name = "animal";
        MovementConstantManager.components.create(animal).constant = MovementConstant.ANIMAL_PARAMS;
        ExplosiveManager.components.create(animal);

        // box2d
        Body rigidBody = SpasholeApp.box2dWorld.createBody(Box2dBodySystem.END_INERTIA_BODY_DEF);
        rigidBody.setUserData(shipUuid);
        Box2dBodyComponent box2dBodyComponent = Box2dBodySystem.components.create(animal);
        box2dBodyComponent.body = rigidBody;
        Shape shape = new CircleShape();
        shape.setRadius(RADIUS);
        rigidBody.createFixture(shape, 10);
        rigidBody.setTransform(worldX, worldY, MathUtilsExt.randomRadian());

        // visual
        Group group = new Group();
        stage.addActor(group);
        Actor2dComponent actor2dComponent = Actor2dManager.components.create(animal);
        actor2dComponent.actor = group;
        group.setOrigin(Align.center);
        group.setScale(SpasholeApp.WORLD_SCALE);

        Image image = new Image(SpasholeAssets.frog);
        group.addActor(image);
        image.setOrigin(Align.center);
        image.setScale(0.75f);
        image.setRotation(-90);
        Actor2dManager.adjustPositionCenterBySize(image);

        RaceManager.components.create(animal).race = race;
        if (race == RaceComp.Race.ANIMAL_AGGR) {
            image.setColor(Color.RED);
            AiDynamicSystem.create(animal.getId());
            GunComponent gunComponent = GunSystem.createGun(SolarSystemScene.ANIMAL_DMG, BARREL_DST
                    , animal.getId());
            gunComponent.bulletColor = Color.GREEN;
            AiFindEnemyHelper.createViewSensor(animal, SpasholeUtils.sceneToWorld(350f));

        } else if (race == RaceComp.Race.ANIMAL_NEUT) {
            image.setColor(Color.GREEN);
            AiDynamicSystem.create(animal.getId());

        } else {
            throw new GdxRuntimeException("Unsupported race: " + race);
        }

        return animal;
    }

}
