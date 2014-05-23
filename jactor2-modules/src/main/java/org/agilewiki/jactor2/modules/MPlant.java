package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.impl.mtPlant.PlantConfiguration;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.modules.impl.MFacilityImpl;
import org.agilewiki.jactor2.modules.impl.MPlantImpl;

public class MPlant {

    public static MFacility getInternalFacility() {
        return MPlantImpl.getSingleton().getInternalFacility();
    }

    public static MFacility getFacility(String name) {
        MFacilityImpl MFacilityImpl = MPlantImpl.getSingleton().getMFacilityImpl(name);
        if (MFacilityImpl == null)
            return null;
        return MFacilityImpl.asMFacility();
    }

    public static AsyncRequest<MFacility> createFacilityAReq(final String _name)
            throws Exception {
        return MFacility.createMFacilityAReq(_name);
    }

    public static String getActivatorClassName(final String _facilityName) {
        return MPlantImpl.getSingleton().getActivatorClassName(_facilityName);
    }

    public static AsyncRequest<ISMap<String>> activatorPropertyAReq(final String _facilityName, final String _className) {
        return MPlantImpl.getSingleton().activatorPropertyAReq(_facilityName, _className);
    }

    public static boolean isAutoStart(String name) {
        return MPlantImpl.getSingleton().isAutoStart(name);
    }

    public static AsyncRequest<ISMap<String>> autoStartAReq(final String _facilityName, final boolean _newValue) {
        return MPlantImpl.getSingleton().autoStartAReq(_facilityName, _newValue);
    }

    public static AsyncRequest<Void> dependencyPropertyAReq(final String _dependentName, final String _dependencyName) {
        return MPlantImpl.getSingleton().dependencyPropertyAReq(_dependentName, _dependencyName);
    }

    public static AsyncRequest<ISMap<String>> purgeFacilitySReq(final String _facilityName) {
        return MPlantImpl.getSingleton().purgeFacilitySReq(_facilityName);
    }

    public static Object getFailed(String name) {
        return MPlantImpl.getSingleton().getFailed(name);
    }

    public static void failFacility(final String _facilityName, final String reason) throws Exception {
        MPlantImpl.getSingleton().failFacility(_facilityName, reason);
    }

    public static AsyncRequest<ISMap<String>> clearFailedAReq(final String _facilityName) {
        return MPlantImpl.getSingleton().failedAReq(_facilityName, null);
    }

    public static boolean isStopped(String name) {
        return MPlantImpl.getSingleton().isStopped(name);
    }

    public static void stopFacility(final String _facilityName) throws Exception {
        MPlantImpl.getSingleton().stopFacility(_facilityName);
    }

    public static AsyncRequest<ISMap<String>> clearStoppedAReq(final String _facilityName) {
        return MPlantImpl.getSingleton().stoppedAReq(_facilityName, false);
    }

    public static AsyncRequest<ISMap<String>> initialLocalMerssageQueueSizePropertyAReq(final String _facilityName, final Integer _value) {
        return MPlantImpl.getSingleton().initialLocalMessageQueueSizePropertyAReq(_facilityName, _value);
    }

    public static AsyncRequest<ISMap<String>> initialBufferSizePropertyAReq(final String _facilityName, final Integer _value) {
        return MPlantImpl.getSingleton().initialBufferSizePropertyAReq(_facilityName, _value);
    }

    public MPlant() throws Exception {
        new MPlantImpl();
    }

    public MPlant(final int _threadCount) throws Exception {
        new MPlantImpl(_threadCount);
    }

    public MPlant(final PlantConfiguration _plantConfiguration) throws Exception {
        new MPlantImpl(_plantConfiguration);
    }
}
