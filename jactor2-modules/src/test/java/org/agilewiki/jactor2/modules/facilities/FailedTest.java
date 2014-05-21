package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MPlant;
import org.agilewiki.jactor2.modules.properties.PropertiesReference;

public class FailedTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            MPlant.activatorPropertyAReq("A", "org.agilewiki.jactor2.modules.facilities.SampleActivator").call();
            MPlant.failFacility("A", "inhibit");
            MPlant.autoStartAReq("A", true).call();
            PropertiesReference propertiesReference = MPlant.getInternalFacility().getPropertiesReference();
            propertiesReference.getReactor().nullSReq().call(); //synchronize for the properties update
            System.out.println("before"+propertiesReference.getImmutable());
            MPlant.clearFailedAReq("A").call();
            propertiesReference.getReactor().nullSReq().call(); //synchronize for the properties update
            System.out.println("after"+propertiesReference.getImmutable());
            Thread.sleep(100); //give the activator a chance to run
        } finally {
            Plant.close();
        }
    }
}
