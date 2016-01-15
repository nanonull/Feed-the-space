package conversion7.spashole.tests

import conversion7.spashole.tests.steps.CoreSteps
import conversion7.spashole.tests.system.TestApp
import spock.lang.Specification

class CoreTests extends Specification {

    CoreSteps coreSteps = new CoreSteps()

    def 'spock works'() {
        when:
        int i = 1
        def arr = [1, 2]

        then:
        assert i == 1
        assert arr.contains(2)
    }

    def 'test test context'() {
        when:
        TestApp.SESSION.put("p1", 1)

        then:
        assert TestApp.SESSION.get("p1") == 1
    }

    def 'pause client core'() {
        given:
        coreSteps.createClientCore()

        when:
        coreSteps.pause()

        then:
        coreSteps.assertClientCoreRenderIsNotInvoked()

        cleanup:
        coreSteps.resume()
    }

    def 'resume client core'() {
        given:
        coreSteps.createClientCore()
        coreSteps.pause()

        when:
        coreSteps.resume()

        then:
        coreSteps.assertClientCoreRenderIsInProgress()
    }


}
