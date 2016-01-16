package conversion7.spashole.game.artemis_odb.systems;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import conversion7.gdxg.core.custom2d.ui_logger.UiLogger;
import conversion7.gdxg.core.utils.MathUtilsExt;
import conversion7.spashole.game.SpasholeApp;
import conversion7.spashole.game.artemis_odb.Tags;
import conversion7.spashole.game.artemis_odb.systems.ai.AiManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodyComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dBodySystem;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorComponent;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dSensorManager;
import conversion7.spashole.game.artemis_odb.systems.box2d.Box2dWorldSystem;
import conversion7.spashole.game.artemis_odb.systems.gun.GunSystem;
import conversion7.spashole.game.stages.SolarSystemScene;
import conversion7.spashole.game.utils.SpasholeUtils;
import org.testng.Assert;

import java.util.UUID;

public class PlayerInputSystem extends BaseSystem {

    private static MovementConstant movementConstant;
    private static final String ENGINE_BROKEN = "Engine broken!";
    public static boolean doNotExecuteCommonActivation;
    private static final float DEGREES_90_AS_RADS = MathUtilsExt.toRadians(90);

    private static final int EVASION_LEFT = Input.Keys.J;
    private static final int EVASION_RIGHT = Input.Keys.L;
    private static final int MOVE_FRWRD = Input.Keys.W;
    private static final int MOVE_BACK = Input.Keys.S;
    private static final int ROTATE_L = Input.Keys.A;
    private static final int ROTATE_R = Input.Keys.D;
    private static final int JOURNAL_KEY = Input.Keys.TAB;
    private static final int AI_SWITCH_KEY = Input.Keys.F3;
    private static final int ATTACK_KEY = Input.Keys.K;
    private static final int ACTIVATE_KEY = Input.Keys.E;

    Vector2 moveDirectionWip = new Vector2();
    Vector2 evasionDirectionWip = new Vector2();
    String movementState;

    public static void setTarget(int entityId) {
        MovementConstantComp movementConstantComp = MovementConstantManager.components.getSafe(entityId);
        Assert.assertNotNull(movementConstantComp);
        SpasholeApp.ARTEMIS_TAGS.register(Tags.PLAYER_ENTITY, entityId);
        PlayerInputSystem.movementConstant = movementConstantComp.constant;
        Assert.assertNotNull(PlayerInputSystem.movementConstant);

        Entity entity = SpasholeApp.ARTEMIS_ENGINE.getEntity(entityId);
        UUID uuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(entity);
        CameraControllerFocusSystem.setTarget(uuid);
    }

    @Override
    protected void processSystem() {
        if (movementConstant == null) {
            return;
        }
        Entity controlledEntity = SpasholeApp.ARTEMIS_TAGS.getEntity(Tags.PLAYER_ENTITY);
        if (controlledEntity == null) {
            return;
        }
        Box2dBodyComponent controlledBodyComponent = Box2dBodySystem.components.get(controlledEntity);

        float bodyAngle = controlledBodyComponent.body.getAngle();
        MathUtilsExt.angleRadiansToVector(bodyAngle, moveDirectionWip);

        if (Gdx.input.isKeyJustPressed(ACTIVATE_KEY)) {

            if (HumanManager.components.has(controlledEntity)) {
                if (Box2dSensorManager.components.has(controlledEntity)) {
                    Box2dSensorComponent triggeredSensorComponent =
                            Box2dSensorManager.components.get(controlledEntity);
                    if (triggeredSensorComponent.detectedEntities.size > 0) {
                        // detected some entity
                        Entity entityCouldBeActivated = SpasholeApp.ARTEMIS_UUIDS.getEntity(
                                triggeredSensorComponent.detectedEntities.first());
                        UiLogger.addInfoLabel("Activated " + NameManager.getSysName(entityCouldBeActivated.getId()));

                        // trigger activation scripts
                        if (ActivationTriggerManager.components.has(entityCouldBeActivated)) {
                            ActivationTriggerComponent activationTriggerComponent =
                                    ActivationTriggerManager.components.get(entityCouldBeActivated);
                            activationTriggerComponent.activatedBy = controlledEntity;
                            activationTriggerComponent.runnable.run();
                            if (activationTriggerComponent.triggerAndRemove) {
                                ActivationTriggerManager.components.remove(entityCouldBeActivated);
                            }
                        }

                        if (doNotExecuteCommonActivation) {
                            doNotExecuteCommonActivation = false;
                        } else {
                            // common actions:
                            if (ShipManager.components.has(entityCouldBeActivated)
                                    && ShipManager.components.get(entityCouldBeActivated).pilotable) {
                                UiLogger.addInfoLabel("Get on ship");
                                UUID controlledEntityUuid = SpasholeApp.ARTEMIS_UUIDS.getUuid(controlledEntity);
                                ShipManager.setPilot(controlledEntityUuid, entityCouldBeActivated);
                            }
                        }
                    }

                } else {
                    UiLogger.addInfoLabel("Nothing to activate");
                }

            } else if (ShipManager.components.has(controlledEntity)) {
                UiLogger.addInfoLabel("Exit from ship");
                ShipComponent shipComponent = ShipManager.components.get(controlledEntity);
                Entity pilotPersonEntity = SpasholeApp.ARTEMIS_UUIDS.getEntity(shipComponent.pilotUuid);
                shipComponent.pilotUuid = null;
                Box2dBodyComponent shipBodyComponent = controlledBodyComponent;

                // place pilot on-foot near ship
                Vector2 shipPosition = shipBodyComponent.body.getPosition();
                Vector2 positionOnCircle = MathUtilsExt.getPositionOnCircle(shipBodyComponent.body.getAngle(),
                        ShipManager.SHIP_RADIUS_WORLD + SolarSystemScene.PERSON_RADIUS + SpasholeUtils.sceneToWorld(4));
                positionOnCircle.add(shipPosition.x, shipPosition.y);

                Position2dSystem.setPosition(pilotPersonEntity.getId()
                        , positionOnCircle.x
                        , positionOnCircle.y);
                EnableEntitySystem.components.create(pilotPersonEntity);

                // activate person controls
                setTarget(pilotPersonEntity.getId());

                // set velocity to person from ship body
                Box2dBodyComponent personBodyComponent = Box2dBodySystem.components.get(pilotPersonEntity);
                Vector2 shipLinearVelocity = shipBodyComponent.body.getLinearVelocity();
                // refresh ship sensors if body sleep
                shipLinearVelocity.x += Box2dWorldSystem.SMALL_VELOCITY;
                personBodyComponent.body.setLinearVelocity(shipLinearVelocity);
            }
        }

        if (Gdx.input.isKeyJustPressed(JOURNAL_KEY)) {
            SpasholeApp.ui.getJournalWindow().show();
        }

        if (Gdx.input.isKeyJustPressed(ATTACK_KEY)) {
            if (GunSystem.components.has(controlledEntity)) {
                GunSystem.components.get(controlledEntity).makeShot();
            }
        }

        if (Gdx.input.isKeyJustPressed(AI_SWITCH_KEY)) {
            AiManager.switchState();
        }

        bodyControls(controlledEntity, controlledBodyComponent);
    }

    private void bodyControls(Entity controlledEntity, Box2dBodyComponent controlledBodyComponent) {
        movementState = null;
        if (ShipManager.components.has(controlledEntity)) {
            ShipComponent shipComponent = ShipManager.components.get(controlledEntity);
            if (!shipComponent.engineWorks) {
                movementState = ENGINE_BROKEN;
            }

            // calculate evasion
            if (Gdx.input.isKeyPressed(EVASION_LEFT)) {
                if (couldMove(movementState)) {
                    if (shipComponent.evasionAvailable) {
                        calcEvasionVector();
                        Box2dBodySystem.move(controlledBodyComponent
                                , evasionDirectionWip
                                , movementConstant.evasionMlt
                                , world.getDelta()
                                , true);
                        shipComponent.usedEvasion(controlledEntity.getId());
                    }
                } else {
                    UiLogger.addInfoLabel(movementState);
                }
            } else if (Gdx.input.isKeyPressed(EVASION_RIGHT)) {
                if (couldMove(movementState)) {
                    if (shipComponent.evasionAvailable) {
                        calcEvasionVector();
                        Box2dBodySystem.move(controlledBodyComponent
                                , evasionDirectionWip
                                , movementConstant.evasionMlt
                                , world.getDelta()
                                , false);
                        shipComponent.usedEvasion(controlledEntity.getId());
                    }
                } else {
                    UiLogger.addInfoLabel(movementState);
                }
            }
        }

        if (Gdx.input.isKeyPressed(MOVE_FRWRD)) {
            if (couldMove(movementState)) {
                Box2dBodySystem.move(controlledBodyComponent
                        , moveDirectionWip
                        , movementConstant.moveMltpl
                        , world.getDelta()
                        , true);
            } else {
                UiLogger.addInfoLabel(movementState);
            }
        }

        if (Gdx.input.isKeyPressed(MOVE_BACK)) {
            if (couldMove(movementState)) {
                Box2dBodySystem.move(controlledBodyComponent
                        , moveDirectionWip
                        , movementConstant.moveMltpl
                        , world.getDelta()
                        , false);
            } else {
                UiLogger.addInfoLabel(movementState);
            }
        }

        if (Gdx.input.isKeyPressed(ROTATE_L)) {
            if (couldMove(movementState)) {
                Box2dBodySystem.rotate(controlledBodyComponent
                        , movementConstant.getTorqueForce()
                        , world.getDelta());
            } else {
                UiLogger.addInfoLabel(movementState);
            }
        }

        if (Gdx.input.isKeyPressed(ROTATE_R)) {
            if (couldMove(movementState)) {
                Box2dBodySystem.rotate(controlledBodyComponent
                        , -movementConstant.getTorqueForce()
                        , world.getDelta());
            } else {
                UiLogger.addInfoLabel(movementState);
            }
        }
    }

    private void calcEvasionVector() {
        float anglePerpend = moveDirectionWip.angleRad() + DEGREES_90_AS_RADS;
        MathUtilsExt.angleRadiansToVector(anglePerpend, evasionDirectionWip);
    }

    private boolean couldMove(String movementState) {
        return movementState == null;
    }

}
