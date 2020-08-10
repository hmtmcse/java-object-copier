package com.hmtmcse.oc.test

import com.hmtmcse.oc.copier.ObjectCopier
import com.hmtmcse.oc.test.data.datatype.ObjectAndPrimitive
import com.hmtmcse.oc.test.data.entitydto.DtoBasic
import com.hmtmcse.oc.test.data.entitydto.EntityBasic
import com.hmtmcse.oc.test.data.entitydto.ValidationDto
import com.hmtmcse.oc.test.data.maplist.MapListSetQueue
import com.hmtmcse.oc.test.data.parentchild.MySon
import spock.lang.Shared
import spock.lang.Specification
import java.time.LocalDate
import java.time.LocalDateTime

class ObjectCopierTest extends Specification {

    @Shared
    ObjectCopier objectCopier = new ObjectCopier()

    def "Test Collection Data Type"(){
        when:
        MapListSetQueue mapListSetQueue = new MapListSetQueue()
        mapListSetQueue.stringList = new ArrayList<>()
        mapListSetQueue.stringList.add("String List 1")
        mapListSetQueue.stringList.add("String List 2")
        mapListSetQueue.stringList.add("String List 3")

        then:
        MapListSetQueue copied = objectCopier.copy(mapListSetQueue, MapListSetQueue.class)
        assert copied.stringList
    }

    def "Test Parent Child Private copy"() {
        when:
        MySon mySon = new MySon()
        mySon.setGrandFatherName("Babul Mia")
        mySon.setFatherName("Touhid")
        mySon.setMyName("Touhid")
        mySon.setSonName("Muhammad")

        then:
        MySon copied = objectCopier.copy(mySon, MySon.class)
        assert copied.getGrandFatherName() == "Babul Mia"
        assert copied.getFatherName() == "Touhid"
        assert copied.getMyName() == "Touhid"
        assert copied.getSonName() == "Muhammad"
    }

    def "Test copy by null object"() {
        when:
        ObjectAndPrimitive objectAndPrimitive = new ObjectAndPrimitive()

        then:
        ObjectAndPrimitive copy = objectCopier.copy(null, ObjectAndPrimitive.class)
        assert copy == null

    }

    def "Test Object and Primitive type copy"() {
        when:
        ObjectAndPrimitive objectAndPrimitive = new ObjectAndPrimitive()
        objectAndPrimitive.stringType = "String Type"
        objectAndPrimitive.integerType = 1000;
        objectAndPrimitive.shortType = 5000
        objectAndPrimitive.longType = 2222l
        objectAndPrimitive.doubleType = 500.55
        objectAndPrimitive.floatType = 300.89f
        objectAndPrimitive.byteType = 1
        objectAndPrimitive.booleanType = false
        objectAndPrimitive.character = 'c'

        objectAndPrimitive.aByte = 0
        objectAndPrimitive.aShort = 1234
        objectAndPrimitive.anInt =  9999;
        objectAndPrimitive.aLong = 11l
        objectAndPrimitive.aFloat = 2.5f
        objectAndPrimitive.aDouble = 99.99
        objectAndPrimitive.aChar = 'x'
        objectAndPrimitive.aBoolean = true

        objectAndPrimitive.bigDecimal = 99999999.99999999
        objectAndPrimitive.bigInteger = 99999999

        objectAndPrimitive.dateType = new Date()
        objectAndPrimitive.localDateTimeType = LocalDateTime.now()
        objectAndPrimitive.localDateType = LocalDate.now()


        then:
        ObjectAndPrimitive copy = objectCopier.copy(objectAndPrimitive, ObjectAndPrimitive.class)

        assert copy.dateType == objectAndPrimitive.dateType
        assert copy.localDateTimeType == objectAndPrimitive.localDateTimeType
        assert copy.localDateType == objectAndPrimitive.localDateType

        assert copy.stringType == "String Type"
        assert copy.integerType == 1000
        assert copy.shortType == 5000
        assert copy.longType == 2222l
        assert copy.doubleType == 500.55
        assert copy.floatType == 300.89f
        assert copy.byteType == 1
        assert !copy.booleanType
        assert copy.character == 'c'

        assert copy.aByte == 0
        assert copy.aShort == 1234
        assert copy.anInt ==  9999;
        assert copy.aLong == 11l
        assert copy.aFloat == 2.5f
        assert copy.aDouble == 99.99
        assert copy.aChar == 'x'
        assert copy.aBoolean

        assert copy.bigDecimal == 99999999.99999999
        assert copy.bigInteger == 99999999

    }

    def "Test Map copy"() {
        when:
        MapListSetQueue mapListSetQueue = new MapListSetQueue()
        mapListSetQueue.stringMap = new LinkedHashMap<>()
        mapListSetQueue.stringMap.put("data-1", "Data 1")
        mapListSetQueue.stringMap.put("data-2", "Data 2")
        mapListSetQueue.stringMap.put("data-3", "Data 3")

        then:
        MapListSetQueue copied = objectCopier.copy(mapListSetQueue, MapListSetQueue.class)
        assert copied.stringMap

    }

    def "Test Entity to DTO copy"() {
        when:
        EntityBasic entityBasic = new EntityBasic()
        entityBasic.name = "Bismillah"
        entityBasic.id =  12345;

        then:
        DtoBasic dtoBasic = objectCopier.copy(entityBasic, DtoBasic.class)
        assert dtoBasic.other == entityBasic.name

    }

    def "Test DTO to Entity copy"() {
        when:
        DtoBasic dtoBasic = new DtoBasic()
        dtoBasic.other = "Bismillah"
        dtoBasic.id =  12345;

        then:
        EntityBasic entityBasic = objectCopier.copy(dtoBasic, EntityBasic.class)
        assert dtoBasic.other == entityBasic.name
    }

    def "Test validation basic"(){
        when:
        ValidationDto validationDto = new ValidationDto()
        validationDto.name = "Mia"
        validationDto.email = "mia.com"

        then:
        objectCopier.validateObject(validationDto)
        assert true
    }

}
