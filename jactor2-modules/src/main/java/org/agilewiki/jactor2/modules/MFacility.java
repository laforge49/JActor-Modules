package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.blades.ismTransactions.ISMReference;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMUpdateTransaction;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMap;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.modules.impl.MFacilityImpl;
import org.agilewiki.jactor2.modules.impl.MPlantImpl;

public class MFacility extends Facility {
    public static AOp<MFacility> createMFacilityAOp(final String _name) throws Exception {
        MPlantImpl plantImpl = MPlantImpl.getSingleton();
        final int initialBufferSize;
        Integer v = (Integer) plantImpl.getProperty(MPlantImpl.initialBufferSizeKey(_name));
        if (v != null)
            initialBufferSize = v;
        else
            initialBufferSize = plantImpl.getInternalFacility().asFacilityImpl().getInitialBufferSize();
        final int initialLocalQueueSize;
        v = (Integer) plantImpl.getProperty(MPlantImpl.initialLocalMessageQueueSizeKey(_name));
        if (v != null)
            initialLocalQueueSize = v;
        else
            initialLocalQueueSize = plantImpl.getInternalFacility().asFacilityImpl().getInitialLocalQueueSize();
        final MFacility mFacility = new MFacility(_name, initialBufferSize, initialLocalQueueSize);
        return new AOp<MFacility>("startFacility", mFacility) {
            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<MFacility> _asyncResponseProcessor) throws Exception {
                _asyncRequestImpl.send(mFacility.asFacilityImpl().startFacilityAOp(), _asyncResponseProcessor, mFacility);
            }
        };
    }

    public static MFacility getMFacility(final Reactor _reactor) {
        if (_reactor instanceof MFacility)
            return (MFacility) _reactor;
        return getMFacility(_reactor.getParentReactor());
    }

    public final ISMReference<String> configuration;

    private MFacility(final String _name, final int _initialOutboxSize, final int _initialLocalQueueSize) throws Exception {
        super(_name, _initialOutboxSize, _initialLocalQueueSize);
        asFacilityImpl().nameSet(name);
        configuration = new ISMReference<>(this);
    }

    public MFacility(final String _name, Void _parentReactor, final int _initialOutboxSize, final int _initialLocalQueueSize) throws Exception {
        super(_name, null, _initialOutboxSize, _initialLocalQueueSize);
        configuration = new ISMReference<>(this);
    }

    @Override
    protected MFacilityImpl createReactorImpl(final NonBlockingReactor _parentReactorImpl,
                                             final int _initialOutboxSize, final int _initialLocalQueueSize) {
        return new MFacilityImpl(_initialOutboxSize, _initialLocalQueueSize);
    }

    public MFacilityImpl asFacilityImpl() {
        return (MFacilityImpl) asReactorImpl();
    }

    public String getName() {
        return asFacilityImpl().getName();
    }

    /**
     * Returns the value of a property.
     *
     * @param propertyName The property name.
     * @return The property value, or null.
     */
    public Object getProperty(final String propertyName) {
        return configuration.getImmutable().get(propertyName);
    }

    public AOp<ISMap<String>> putPropertyAOp(final String _propertyName,
                                             final String _propertyValue) {
        return new ISMUpdateTransaction<String>(_propertyName, _propertyValue).
                applyAOp(configuration);
    }

    public AOp<ISMap<String>> putPropertyAOp(final String _propertyName,
                                             final String _expectedValue,
                                             final String _propertyValue) {
        return new ISMUpdateTransaction<String>(_propertyName, _propertyValue, _expectedValue).
                applyAOp(configuration);
    }

    public String toString() {
        return getName();
    }
}
