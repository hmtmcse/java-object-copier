package com.hmtmcse.oc.test

import com.hmtmcse.oc.reflection.ReflectionProcessor
import com.hmtmcse.oc.test.data.parentchild.MySon
import spock.lang.Shared
import spock.lang.Specification


class ReflectionProcessorTest extends Specification {

    @Shared
    ReflectionProcessor reflectionProcessor = new ReflectionProcessor()


    def "Test All Super Class"() {
        when:
        List<Class<?>> list = reflectionProcessor.getAllSuperClass(MySon.class)

        then:
        def klasses = ["Father", "GrandFather", "MySelf", "Object"]
        list.each { Class<?> klass ->
            println(klass.getSimpleName())
            assert klasses.contains(klass.getSimpleName())
            klasses.remove(klass.getSimpleName())
        }
        assert klasses.size() == 0
    }

}
