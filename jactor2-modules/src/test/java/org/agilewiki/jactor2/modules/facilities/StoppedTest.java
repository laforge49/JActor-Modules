package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.service.ClassLoaderService;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMReference;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MPlant;

public class StoppedTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            MPlant.activatorPropertyAOp("A", "org.agilewiki.jactor2.a.SampleActivator").call();
            MPlant.stopFacility("A");
            MPlant.autoStartAOp("A", true).call();
            ISMReference<String> propertiesReference = MPlant.getInternalFacility().configuration;
            propertiesReference.getReactor().nullSOp().call(); //synchronize for the properties update
            System.out.println("before"+propertiesReference.getImmutable());
            MPlant.clearStoppedAOp("A").call();
            propertiesReference.getReactor().nullSOp().call(); //synchronize for the properties update
            System.out.println("after"+propertiesReference.getImmutable());
            Thread.sleep(500); //give the activator a chance to run
        } finally {
            Plant.close();
        }
    }
}
