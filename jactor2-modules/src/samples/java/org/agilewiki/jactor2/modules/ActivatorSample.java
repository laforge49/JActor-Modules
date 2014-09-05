package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.impl.Plant;

import java.io.File;

public class ActivatorSample {
    public static void main(String[] args) throws Exception {
        new MPlant();
        try {
            String fn = "jactor2-a/target/jactor2-a-0.0.1.jar";
            if (!(new File(fn).exists()))
                fn = "../" + fn;
            MPlant.resourcePropertyAOp("a", fn).call();
            MPlant.activatorPropertyAOp("a", "org.agilewiki.jactor2.a.SampleActivator").call();
            MPlant.createMFacilityAOp("a").call();
        } finally {
            Plant.close();
        }
    }
}
