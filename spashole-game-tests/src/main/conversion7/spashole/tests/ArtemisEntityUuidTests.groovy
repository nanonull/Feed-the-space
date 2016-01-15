package conversion7.spashole.tests

import conversion7.gdxg.core.utils.Utils
import conversion7.spashole.game.SpasholeApp
import conversion7.spashole.tests.artemis_odb.TestSystem
import conversion7.spashole.tests.steps.CoreSteps
import spock.lang.Specification

class ArtemisEntityUuidTests extends Specification {

    CoreSteps coreSteps = new CoreSteps()

    void 'test 1'() {
        given:
        coreSteps.createClientCore()

        def entity1 = SpasholeApp.ARTEMIS_ENGINE.createEntity()
        def uuid1 = SpasholeApp.ARTEMIS_UUIDS.getUuid(entity1)
        TestSystem.components.create(entity1)
        assert TestSystem.components.has(entity1)
        assert SpasholeApp.ARTEMIS_UUIDS.getEntity(uuid1) == entity1

        when:
        println 'delete entity 1'
        coreSteps.waitForNextRenderFrame()
        SpasholeApp.ARTEMIS_ENGINE.delete(entity1.id)
        coreSteps.waitForNextRenderFrame()

        println 'and create new entity'
        def entity2 = SpasholeApp.ARTEMIS_ENGINE.createEntity()

        then:
        Utils.sleepThread(500)
        assert !TestSystem.components.has(entity2)
        assert !TestSystem.components.has(entity1)
        println '...but uuid was cleared'
        assert SpasholeApp.ARTEMIS_UUIDS.getEntity(uuid1) == null
    }
}
