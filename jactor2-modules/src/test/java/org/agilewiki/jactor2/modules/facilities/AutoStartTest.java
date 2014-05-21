package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MPlant;
import org.agilewiki.jactor2.modules.properties.PropertiesReference;

public class AutoStartTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            MPlant.activatorPropertyAReq("B", "org.agilewiki.jactor2.modules.facilities.SampleActivator").call();
            MPlant.dependencyPropertyAReq("B", "A").call();
            MPlant.autoStartAReq("B", true).call();
            MPlant.autoStartAReq("A", true).call();
            PropertiesReference propertiesReference = MPlant.getInternalFacility().getPropertiesReference();
            propertiesReference.getReactor().nullSReq().call(); //synchronize for the properties update
            ISMap<String> properties = propertiesReference.getImmutable();
            System.out.println(properties);
            Thread.sleep(100); //give the activator a chance to run
        } finally {
            Plant.close();
        }
    }
}
