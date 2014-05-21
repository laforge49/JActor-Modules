package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;

public class ActivatorFailure {
    static public void main(final String[] _args) throws Exception {
        new MPlant();
        try {
            MPlant.activatorPropertyAReq("a", "NoSuchActivator").call();
            try {
                MFacility.createMFacilityAReq("a").call();
            } catch (ReactorClosedException e) {
                MFacility MFacility = MPlant.getInternalFacility();
                MFacility.nullSReq().call(); //synchronize for the properties update
                System.out.println(MFacility.getPropertiesReference().getImmutable());
            }
        } finally {
            Plant.close();
        }
    }
}
