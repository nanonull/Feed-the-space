package conversion7.spashole.tests

import conversion7.gdxg.core.utils.Utils
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.tests.artemis_odb.TestSystem
import conversion7.spashole.tests.steps.CoreSteps
import spock.lang.Specification

class ArtemisEntityIdPoolingTests extends Specification {

    CoreSteps coreSteps = new CoreSteps()

    void 'test entityId could be pooled after destroy (without wait next frame)'() {
        given:
        coreSteps.createClientCore()

        def entityId1 = SpasholeApp.ARTEMIS_ENGINE.create()
        TestSystem.components.create(entityId1)
        assert TestSystem.components.has(entityId1)

        when:
        println 'delete entity 1'
        SpasholeApp.ARTEMIS_ENGINE.delete(entityId1)

        println 'and create new entity'
        def entityId2 = SpasholeApp.ARTEMIS_ENGINE.create()

        then:
        Utils.sleepThread(500)
        println 'entity id was not pooled during '
        assert entityId1 != entityId2
        println 'however component was removed'
        assert !TestSystem.components.has(entityId1)
    }

}
