package com.hmtmcse.oc.test

import com.hmtmcse.oc.copier.ObjectCopier
import com.hmtmcse.oc.test.data.datatype.ObjectAndPrimitive
import com.hmtmcse.oc.test.data.ftest.ObjectDataType
import com.hmtmcse.oc.test.data.ftest.PrimitiveDataType
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class OCBasicTest extends Specification {

    @Shared
    ObjectCopier objectCopier = new ObjectCopier()

    def "Test PrimitiveDataType Source Object and Destination Class"() {
        when:
        PrimitiveDataType primitiveDataType = new PrimitiveDataType();
        primitiveDataType.aByte = 100
        primitiveDataType.aShort = 520

        then:
        PrimitiveDataType copy = objectCopier.copy(primitiveDataType, PrimitiveDataType.class)

        assert primitiveDataType.aByte == copy.aByte
        assert primitiveDataType.aShort == copy.aShort
        assert primitiveDataType.anInt == copy.anInt
        assert primitiveDataType.aLong == copy.aLong
        assert primitiveDataType.aFloat == copy.aFloat
        assert primitiveDataType.aDouble == copy.aDouble
        assert primitiveDataType.aChar == copy.aChar
        assert primitiveDataType.aBoolean == copy.aBoolean

    }

    def "Test PrimitiveDataType Source Object and Destination Object"() {
        when:
        PrimitiveDataType primitiveDataType = new PrimitiveDataType();
        primitiveDataType.aByte = 100
        primitiveDataType.aShort = 520

        PrimitiveDataType copy = new PrimitiveDataType()
        copy.anInt = 120
        copy.anInt = 500
        copy.aBoolean = true
        copy = objectCopier.copy(primitiveDataType, copy)

        then:
        assert primitiveDataType.aByte == copy.aByte
        assert primitiveDataType.aShort == copy.aShort
        assert primitiveDataType.anInt == copy.anInt
        assert primitiveDataType.aLong == copy.aLong
        assert primitiveDataType.aFloat == copy.aFloat
        assert primitiveDataType.aDouble == copy.aDouble
        assert primitiveDataType.aChar == copy.aChar
        assert primitiveDataType.aBoolean == copy.aBoolean

    }

    def "Test ObjectDataType Source Object and Destination Class"() {
        when:
        ObjectDataType objectDataType = new ObjectDataType()
        objectDataType.stringType = "String Type"
        objectDataType.integerType = 1992
        objectDataType.shortType = 9988
        objectDataType.longType = 987456l
        objectDataType.doubleType = 500.55
        objectDataType.floatType = 300.89f
        objectDataType.byteType = 1
        objectDataType.booleanType = true
        objectDataType.character = 'a'

        objectDataType.dateType = new Date()
        objectDataType.localDateType = LocalDate.now()
        objectDataType.localDateTimeType = LocalDateTime.now()

        objectDataType.bigDecimal = 99999999.99999999
        objectDataType.bigInteger = 99999999

        then:
        ObjectDataType copy = objectCopier.copy(objectDataType, ObjectDataType.class)

        assert objectDataType.stringType == copy.stringType
        assert objectDataType.integerType == copy.integerType
        assert objectDataType.shortType == copy.shortType
        assert objectDataType.longType == copy.longType
        assert objectDataType.doubleType == copy.doubleType
        assert objectDataType.floatType == copy.floatType
        assert objectDataType.byteType == copy.byteType
        assert objectDataType.booleanType == copy.booleanType

        assert objectDataType.dateType == copy.dateType
        assert objectDataType.localDateType == copy.localDateType
        assert objectDataType.localDateTimeType == copy.localDateTimeType

        assert objectDataType.bigDecimal == copy.bigDecimal
        assert objectDataType.bigInteger == copy.bigInteger

    }

    def "Test ObjectDataType Source Object and Destination Object"() {

        when:
        ObjectDataType objectDataType = new ObjectDataType()
        objectDataType.stringType = "String Type"
        objectDataType.integerType = 1992
        objectDataType.shortType = 9988
        objectDataType.doubleType = 500.55
        objectDataType.byteType = 1
        objectDataType.character = 'a'

        objectDataType.dateType = new Date()
        objectDataType.localDateType = LocalDate.now()
        objectDataType.localDateTimeType = LocalDateTime.now()

        objectDataType.bigDecimal = 99999999.99999999
        objectDataType.bigInteger = 99999999

        ObjectDataType copy = new ObjectDataType();
        copy.longType = 987456l
        copy.floatType = 300.89f
        copy.booleanType = true
        copy = objectCopier.copy(objectDataType, copy)

        then:
        assert objectDataType.longType != copy.longType
        assert objectDataType.floatType != copy.floatType
        assert objectDataType.booleanType != copy.booleanType

        assert objectDataType.stringType == copy.stringType
        assert objectDataType.integerType == copy.integerType
        assert objectDataType.shortType == copy.shortType
        assert objectDataType.doubleType == copy.doubleType
        assert objectDataType.byteType == copy.byteType

        assert objectDataType.dateType == copy.dateType
        assert objectDataType.localDateType == copy.localDateType
        assert objectDataType.localDateTimeType == copy.localDateTimeType

        assert objectDataType.bigDecimal == copy.bigDecimal
        assert objectDataType.bigInteger == copy.bigInteger

        assert objectDataType.stringTypeNull == null
        assert objectDataType.integerTypeNull == null
        assert objectDataType.shortTypeNull == null
        assert objectDataType.longTypeNull == null
        assert objectDataType.doubleTypeNull == null
        assert objectDataType.floatTypeNull == null
        assert objectDataType.byteTypeNull == null
        assert objectDataType.booleanTypeNull == null
        assert objectDataType.characterNull == null

    }


}
