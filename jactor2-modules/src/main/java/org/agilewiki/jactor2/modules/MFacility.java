package org.agilewiki.jactor2.modules;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.TSSMAppendTransaction;
import org.agilewiki.jactor2.common.services.ClassLoaderService;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.blades.transmutable.tssmTransactions.TSSMReference;
import org.agilewiki.jactor2.core.blades.transmutable.tssmTransactions.TSSMUpdateTransaction;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.modules.impl.MFacilityImpl;
import org.agilewiki.jactor2.modules.impl.MPlantImpl;
import org.xeustechnologies.jcl.CompositeProxyClassLoader;

import java.util.Collection;
import java.util.Iterator;

public class MFacility extends CFacility {
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
                                              final AsyncResponseProcessor<MFacility> _asyncResponseProcessor)
                    throws Exception {
                _asyncRequestImpl.send(mFacility.asFacilityImpl().startFacilityAOp(), _asyncResponseProcessor, mFacility);
            }
        };
    }

    public static MFacility getMFacility(final Reactor _reactor) {
        if (_reactor instanceof MFacility)
            return (MFacility) _reactor;
        return getMFacility(_reactor.getParentReactor());
    }

    public final TSSMReference<String> configuration;

    private MFacility(final String _name, final int _initialOutboxSize, final int _initialLocalQueueSize) throws Exception {
        super(_name, _initialOutboxSize, _initialLocalQueueSize);
        asFacilityImpl().nameSet(name);
        configuration = new TSSMReference<>(this);
    }

    public MFacility(final String _name, Void _parentReactor, final int _initialOutboxSize, final int _initialLocalQueueSize) throws Exception {
        super(_name, null, _initialOutboxSize, _initialLocalQueueSize);
        configuration = new TSSMReference<>(this);
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
    public String getProperty(final String propertyName) {
        return configuration.getUnmodifiable().get(propertyName);
    }

    public AOp<Void> putPropertyAOp(final String _propertyName,
                                             final String _propertyValue) {
        return configuration.
                applyAOp(new TSSMUpdateTransaction<String>(_propertyName, _propertyValue));
    }

    public AOp<Void> appendPropertyAOp(final String _prefix,
                                             final String _propertyValue) {
        return configuration.
                applyAOp(new TSSMAppendTransaction<String>(_prefix, _propertyValue));
    }

    public AOp<Void> putPropertyAOp(final String _propertyName,
                                             final String _expectedValue,
                                             final String _propertyValue) {
        return configuration.
                applyAOp(new TSSMUpdateTransaction<String>(_propertyName, _propertyValue, _expectedValue));
    }

    public String toString() {
        return getName();
    }

    public CompositeProxyClassLoader getCCL() throws Exception {
        return ClassLoaderService.getClassLoaderService(this).ccl;
    }

    public Collection<String> dependencyNames() {
        return MPlantImpl.getSingleton().dependencyNames(getName());
    }

    @Override
    public NamedBlade getNamedBlade(final String _bladeName) {
        NamedBlade namedBlade = super.getNamedBlade(_bladeName);
        if (namedBlade != null)
            return namedBlade;
        Iterator<String> it = dependencyNames().iterator();
        while (it.hasNext()) {
            String dependencyName = it.next();
            MFacility facility = MPlant.getFacility(dependencyName);
            namedBlade = facility.getNamedBlade(_bladeName);
            if (namedBlade != null)
                return namedBlade;
        }
        return null;
    }

    @Override
    public WidgetFactory getWidgetFactory(final String _factoryKey) {
        WidgetFactory widgetFactory = super.getWidgetFactory(_factoryKey);
        if (widgetFactory != null)
            return widgetFactory;
        Iterator<String> it = dependencyNames().iterator();
        while (it.hasNext()) {
            String dependencyName = it.next();
            MFacility facility = MPlant.getFacility(dependencyName);
            widgetFactory = facility.getWidgetFactory(_factoryKey);
            if (widgetFactory != null)
                return widgetFactory;
        }
        return null;
    }
}
