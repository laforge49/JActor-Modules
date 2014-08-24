package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.common.service.ClassLoaderService;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.modules.facilities.SampleActivator;

public class ActivatorSample {
    public static void main(String[] args) throws Exception {
        new MPlant();
        try {
            ClassLoaderService.register();
            MPlant.activatorPropertyAOp("a", SampleActivator.class.getName()).call();
            MPlant.createMFacilityAOp("a").call();
        } finally {
            Plant.close();
        }
    }
}
