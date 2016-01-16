package conversion7.spashole.game.artemis_odb.systems.box2d
import groovy.transform.CompileStatic

@CompileStatic
class Box2dWorldSystemTest extends GroovyTestCase {

    void testDefaultMasks() {
        short categoryBitsA = 0x0001;
        short maskBitsA = -1;
        short categoryBitsB = 0x0001;
        short maskBitsB = -1;

        assert canCollide(categoryBitsA, maskBitsA, categoryBitsB, maskBitsB)

    }

    // FIXME
    void ignoreTestActiveBodyMask() {
        short categoryBitsA = Box2dBodySystem.ACTIVE_BODY;
        short maskBitsA = Box2dBodySystem.ALL_MASK;
        short categoryBitsB = Box2dBodySystem.ACTIVE_BODY;
        short maskBitsB = Box2dBodySystem.ALL_MASK;

        assert !canCollide(categoryBitsA, maskBitsA, categoryBitsB, maskBitsB)

    }

    void testInactiveBodyMask() {
        short categoryBitsA = 0x0001;
        short maskBitsA = -1;
        short categoryBitsB = Box2dBodySystem.INACTIVE_BODY;
        short maskBitsB = Box2dBodySystem.NOBODY_MASK;

        assert !canCollide(categoryBitsA, maskBitsA, categoryBitsB, maskBitsB)

    }

    void testInactiveBodyMask2() {
        short categoryBitsA = Box2dBodySystem.INACTIVE_BODY;
        short maskBitsA = Box2dBodySystem.NOBODY_MASK;
        short categoryBitsB = 0x0001;
        short maskBitsB = -1;

        assert !canCollide(categoryBitsA, maskBitsA, categoryBitsB, maskBitsB)

    }

    def canCollide(short categoryBitsA, short maskBitsA, short categoryBitsB, short maskBitsB) {
        if ((categoryBitsA & maskBitsB) != 0 && (categoryBitsB & maskBitsA) != 0) {
            return true
        }

        return false;
    }
}
