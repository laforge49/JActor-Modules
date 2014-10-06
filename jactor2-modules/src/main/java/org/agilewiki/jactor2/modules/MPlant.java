package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.blades.ismTransactions.ISMap;
import org.agilewiki.jactor2.core.impl.mtPlant.PlantConfiguration;
import org.agilewiki.jactor2.core.plant.PlantBase;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.modules.impl.MFacilityImpl;
import org.agilewiki.jactor2.modules.impl.MPlantImpl;

public class MPlant extends PlantBase {

    public static MFacility getInternalFacility() {
        return MPlantImpl.getSingleton().getInternalFacility();
    }

    public static MFacility getMFacility(String name) {
        MFacilityImpl MFacilityImpl = MPlantImpl.getSingleton().getMFacilityImpl(name);
        if (MFacilityImpl == null)
            return null;
        return MFacilityImpl.asMFacility();
    }

    public static AOp<MFacility> createMFacilityAOp(final String _name)
            throws Exception {
        return MFacility.createMFacilityAOp(_name);
    }

    public static String getActivatorClassName(final String _facilityName) {
        return MPlantImpl.getSingleton().getActivatorClassName(_facilityName);
    }

    public static AOp<Void> activatorPropertyAOp(final String _facilityName, final String _className) {
        return MPlantImpl.getSingleton().activatorPropertyAOp(_facilityName, _className);
    }

    public static boolean isAutoStart(String name) {
        return MPlantImpl.getSingleton().isAutoStart(name);
    }

    public static AOp<Void> autoStartAOp(final String _facilityName, final boolean _newValue) {
        return MPlantImpl.getSingleton().autoStartAOp(_facilityName, _newValue);
    }

    public static AOp<Void> dependencyPropertyAOp(final String _dependentName, final String _dependencyName) {
        return MPlantImpl.getSingleton().dependencyPropertyAOp(_dependentName, _dependencyName);
    }

    public static AOp<Void> resourcePropertyAOp(final String _facilityName, final String _resource) {
        return MPlantImpl.getSingleton().resourcePropertyAOp(_facilityName, _resource);
    }

    public static AOp<Void> purgeFacilityAOp(final String _facilityName) {
        return MPlantImpl.getSingleton().purgeFacilityAOp(_facilityName);
    }

    public static Object getFailed(String name) {
        return MPlantImpl.getSingleton().getFailed(name);
    }

    public static void failFacility(final String _facilityName, final String reason) throws Exception {
        MPlantImpl.getSingleton().failFacility(_facilityName, reason);
    }

    public static AOp<Void> clearFailedAOp(final String _facilityName) {
        return MPlantImpl.getSingleton().failedAOp(_facilityName, null);
    }

    public static boolean isStopped(String name) {
        return MPlantImpl.getSingleton().isStopped(name);
    }

    public static void stopFacility(final String _facilityName) throws Exception {
        MPlantImpl.getSingleton().stopFacility(_facilityName);
    }

    public static AOp<Void> clearStoppedAOp(final String _facilityName) {
        return MPlantImpl.getSingleton().stoppedAOp(_facilityName, false);
    }

    public static AOp<Void> initialLocalMessageQueueSizePropertyAOp(final String _facilityName, final Integer _value) {
        return MPlantImpl.getSingleton().initialLocalMessageQueueSizePropertyAOp(_facilityName, _value);
    }

    public static AOp<Void> initialBufferSizePropertyAOp(final String _facilityName, final Integer _value) {
        return MPlantImpl.getSingleton().initialBufferSizePropertyAOp(_facilityName, _value);
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
