package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.common.service.ClassLoaderService;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;

public class ActivatorFailure {
    static public void main(final String[] _args) throws Exception {
        new MPlant();
        try {
            ClassLoaderService.register();
            MPlant.activatorPropertyAOp("a", "NoSuchActivator").call();
            MFacility mFacility = MPlant.getInternalFacility();
            mFacility.nullSOp().call(); //synchronize for the properties update
            System.out.println(mFacility.configuration.getImmutable());
            try {
                MFacility.createMFacilityAOp("a").call();
            } catch (ReactorClosedException e) {
                mFacility.nullSOp().call(); //synchronize for the properties update
                System.out.println(mFacility.configuration.getImmutable());
            }
        } finally {
            Plant.close();
        }
    }
}
