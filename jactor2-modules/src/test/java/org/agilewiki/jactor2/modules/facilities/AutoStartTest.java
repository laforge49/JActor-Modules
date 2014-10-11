package org.agilewiki.jactor2.modules.facilities;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.blades.transmutable.tssmTransactions.TSSMReference;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.MPlant;

import java.io.File;
import java.util.SortedMap;

public class AutoStartTest extends TestCase {
    public void test() throws Exception {
        new MPlant();
        try {
            String fn = "jactor2-a/target/jactor2-a-0.2.0.jar";
            if (!(new File(fn).exists()))
                fn = "../" + fn;
            MPlant.resourcePropertyAOp("A", fn).call();
            MPlant.activatorPropertyAOp("B", "org.agilewiki.jactor2.a.SampleActivator").call();
            MPlant.dependencyPropertyAOp("B", "A").call();
            MPlant.autoStartAOp("B", true).call();
            MPlant.autoStartAOp("A", true).call();
            TSSMReference<String> propertiesReference = MPlant.getInternalFacility().configuration;
            propertiesReference.getReactor().nullSOp().call(); //synchronize for the properties update
            SortedMap<String, String> properties = propertiesReference.getUnmodifiable();
            System.out.println(properties);
            Thread.sleep(100); //give the activator a chance to run
        } finally {
            Plant.close();
        }
    }
}
