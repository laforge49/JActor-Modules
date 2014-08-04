package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.impl.Plant;

public class ActivatorSample {
    public static void main(String[] args) throws Exception {
        new MPlant();
        try {
            MPlant.activatorPropertyAOp("a", "org.agilewiki.jactor2.modules.SampleActivator").call();
            MPlant.createMFacilityAOp("a").call();
        } finally {
            Plant.close();
        }
    }
}
