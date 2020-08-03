package com.hmtmcse.oc.test

import com.hmtmcse.oc.copier.ObjectCopier
import com.hmtmcse.oc.test.data.parentchild.MySon
import spock.lang.Shared
import spock.lang.Specification

class ObjectCopierTest extends Specification {

    @Shared
    ObjectCopier objectCopier = new ObjectCopier()

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

}
