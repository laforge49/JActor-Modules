package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.transmutable.tssmTransactions.TSSMReference;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MPlant;

import java.io.File;

public class StoppedTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            String fn = "jactor2-a/target/jactor2-a-0.0.1.jar";
            if (!(new File(fn).exists()))
                fn = "../" + fn;
            MPlant.resourcePropertyAOp("A", fn).call();
            MPlant.activatorPropertyAOp("A", "org.agilewiki.jactor2.a.SampleActivator").call();
            MPlant.stopFacility("A");
            MPlant.autoStartAOp("A", true).call();
            TSSMReference<String> propertiesReference = MPlant.getInternalFacility().configuration;
            propertiesReference.getReactor().nullSOp().call(); //synchronize for the properties update
            System.out.println("before" + propertiesReference.getUnmodifiable());
            MPlant.clearStoppedAOp("A").call();
            propertiesReference.getReactor().nullSOp().call(); //synchronize for the properties update
            System.out.println("after" + propertiesReference.getUnmodifiable());
            Thread.sleep(500); //give the activator a chance to run
        } finally {
            Plant.close();
        }
    }
}
