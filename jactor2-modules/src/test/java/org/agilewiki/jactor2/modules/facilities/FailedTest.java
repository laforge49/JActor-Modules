package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMReference;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MPlant;

public class FailedTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            MPlant.activatorPropertyAOp("A", "org.agilewiki.jactor2.modules.facilities.SampleActivator").call();
            MPlant.failFacility("A", "inhibit");
            MPlant.autoStartAOp("A", true).call();
            ISMReference<String> propertiesReference = MPlant.getInternalFacility().configuration;
            propertiesReference.getReactor().nullSOp().call(); //synchronize for the properties update
            System.out.println("before"+propertiesReference.getImmutable());
            MPlant.clearFailedAOp("A").call();
            propertiesReference.getReactor().nullSOp().call(); //synchronize for the properties update
            System.out.println("after"+propertiesReference.getImmutable());
            Thread.sleep(100); //give the activator a chance to run
        } finally {
            Plant.close();
        }
    }
}
