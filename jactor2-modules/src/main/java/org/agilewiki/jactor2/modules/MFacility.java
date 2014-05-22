package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.modules.impl.MFacilityImpl;
import org.agilewiki.jactor2.modules.impl.MPlantImpl;
import org.agilewiki.jactor2.modules.properties.PropertiesReference;

public class MFacility extends Facility {
    public static AsyncRequest<MFacility> createMFacilityAReq(final String _name) throws Exception {
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
        return new AsyncRequest<MFacility>(mFacility) {
            @Override
            public void processAsyncRequest() throws Exception {
                send(mFacility.asFacilityImpl().startFacilityAReq(), this, mFacility);
            }
        };
    }

    public static MFacility asMFacility(final Reactor _reactor) {
        if (_reactor instanceof MFacility)
            return (MFacility) _reactor;
        return asMFacility(_reactor.getParentReactor());
    }

    private MFacility(final String _name, final int _initialOutboxSize, final int _initialLocalQueueSize) throws Exception {
        super(_name, _initialOutboxSize, _initialLocalQueueSize);
        asFacilityImpl().nameSet(name);
    }

    public MFacility(final String _name, Void _parentReactor, final int _initialOutboxSize, final int _initialLocalQueueSize) throws Exception {
        super(_name, null, _initialOutboxSize, _initialLocalQueueSize);
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

    public PropertiesReference getPropertiesReference() {
        return asFacilityImpl().getPropertiesReference();
    }

    public Object getProperty(final String propertyName) {
        return asFacilityImpl().getProperty(propertyName);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final Boolean _expectedValue,
                                              final Boolean _propertyValue) {
        return asFacilityImpl().putPropertyAReq(_propertyName, _propertyValue, _expectedValue);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final String _expectedValue,
                                              final String _propertyValue) {
        return asFacilityImpl().putPropertyAReq(_propertyName, _propertyValue, _expectedValue);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final String _propertyValue) {
        return asFacilityImpl().putPropertyAReq(_propertyName, _propertyValue);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final Boolean _propertyValue) {
        return asFacilityImpl().putPropertyAReq(_propertyName, _propertyValue);
    }
}
