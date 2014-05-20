package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.Facility;
import org.agilewiki.jactor2.modules.MPlant;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;
import org.agilewiki.jactor2.modules.properties.transactions.PropertiesReference;
import org.agilewiki.jactor2.modules.transactions.properties.PropertiesProcessor;

public class DependencyTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            MPlant.dependencyPropertyAReq("B", "A").call();
            MPlant.dependencyPropertyAReq("C", "B").call();
            final Facility a = MPlant.createFacilityAReq("A")
                    .call();
            final Facility b = MPlant.createFacilityAReq("B")
                    .call();
            final Facility c = MPlant.createFacilityAReq("C")
                    .call();
            PropertiesReference propertiesReference = MPlant.getInternalFacility().getPropertiesReference();
            ImmutableProperties properties = propertiesReference.getImmutable();
            System.out.println("before: "+properties);
            MPlant.purgeFacilitySReq("A").call();
            MPlant.getInternalFacility().getPropertiesReference().getReactor().nullSReq().call(); //synchronize for the properties update
            properties = propertiesReference.getImmutable();
            System.out.println("after: "+properties);
        } finally {
            Plant.close();
        }
    }
}
