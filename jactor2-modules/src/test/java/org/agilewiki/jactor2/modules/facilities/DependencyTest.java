package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMReference;
import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MFacility;
import org.agilewiki.jactor2.modules.MPlant;

public class DependencyTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            MPlant.dependencyPropertyAReq("B", "A").call();
            MPlant.dependencyPropertyAReq("C", "B").call();
            final MFacility a = MPlant.createFacilityAReq("A")
                    .call();
            final MFacility b = MPlant.createFacilityAReq("B")
                    .call();
            final MFacility c = MPlant.createFacilityAReq("C")
                    .call();
            ISMReference<String> propertiesReference = MPlant.getInternalFacility().getISMReference();
            ISMap<String> properties = propertiesReference.getImmutable();
            System.out.println("before: "+properties);
            MPlant.purgeFacilitySReq("A").call();
            MPlant.getInternalFacility().getISMReference().getReactor().nullSReq().call(); //synchronize for the properties update
            properties = propertiesReference.getImmutable();
            System.out.println("after: "+properties);
        } finally {
            Plant.close();
        }
    }
}
