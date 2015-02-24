package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.transmutable.tssmTransactions.TSSMReference;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MFacility;
import org.agilewiki.jactor2.modules.MPlant;

import java.util.SortedMap;

public class DependencyTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            MPlant.dependencyPropertyAOp("B", "A").call();
            MPlant.dependencyPropertyAOp("C", "B").call();
            final MFacility a = MPlant.createMFacilityAOp("A")
                    .call();
            Thread.sleep(200);
            final MFacility b = MPlant.createMFacilityAOp("B")
                    .call();
            Thread.sleep(200);
            final MFacility c = MPlant.createMFacilityAOp("C")
                    .call();
            MPlant.getInternalFacility().unregisterBladeSOp("C").call();
            TSSMReference<String> propertiesReference = MPlant.getInternalFacility().configuration;
            SortedMap<String, String> properties = propertiesReference.getUnmodifiable();
            System.out.println("before: " + properties);
            MPlant.purgeFacilityAOp("C").call();
            MPlant.getInternalFacility().configuration.getReactor().nullSOp().call(); //synchronize for the properties update
            properties = propertiesReference.getUnmodifiable();
            System.out.println("after: " + properties);
        } finally {
            Thread.sleep(200);
            Plant.close();
        }
    }
}
