package com.hmtmcse.oc.test

import com.hmtmcse.oc.reflection.ReflectionProcessor
import com.hmtmcse.oc.test.data.parentchild.MySon
import spock.lang.Shared
import spock.lang.Specification

import java.lang.reflect.Field


class ReflectionProcessorTest extends Specification {

    @Shared
    ReflectionProcessor reflectionProcessor = new ReflectionProcessor()


    def "Test Get All Super Class"() {
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
        println("\n")
    }

    def "Test Get All Class"() {
        when:
        List<Class<?>> list = reflectionProcessor.getAllClass(MySon.class)

        then:
        def klasses = ["MySon", "Father", "GrandFather", "MySelf", "Object"]
        list.each { Class<?> klass ->
            println(klass.getSimpleName())
            assert klasses.contains(klass.getSimpleName())
            klasses.remove(klass.getSimpleName())
        }
        assert klasses.size() == 0
        println("\n")
    }

    def "Test getAllField"(){

        when:
        List<Field> list = reflectionProcessor.getAllField(MySon.class)

        then:
        def names = ["grandFatherName", "fatherName", "myName", "sonName"]
        list.each { Field field ->
            println(field.getName())
            assert names.contains(field.getName())
            names.remove(field.getName())
        }
        assert names.size() == 0
        println("\n")

    }

}
