package org.agilewiki.jactor2.modules.impl;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.closeable.Closeable;
import org.agilewiki.jactor2.core.impl.mtReactors.NonBlockingReactorMtImpl;
import org.agilewiki.jactor2.core.impl.mtReactors.ReactorMtImpl;
import org.agilewiki.jactor2.core.plant.PlantImpl;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.ExceptionHandler;
import org.agilewiki.jactor2.modules.Activator;
import org.agilewiki.jactor2.modules.DependencyNotPresentException;
import org.agilewiki.jactor2.modules.MFacility;
import org.agilewiki.jactor2.modules.MPlant;
import org.agilewiki.jactor2.modules.properties.ImmutablePropertyChanges;
import org.agilewiki.jactor2.modules.properties.PropertiesReference;
import org.agilewiki.jactor2.modules.properties.PropertyChange;
import org.agilewiki.jactor2.modules.properties.UpdatePropertyTransaction;
import org.agilewiki.jactor2.modules.pubSub.SubscribeAReq;
import org.agilewiki.jactor2.modules.pubSub.Subscription;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.SortedMap;

public class MFacilityImpl extends NonBlockingReactorMtImpl {
    protected PropertiesReference propertiesReference;

    private MPlantImpl plantImpl;

    private MFacilityImpl plantMFacilityImpl;

    private String name;

    public MFacilityImpl(final int _initialOutboxSize, final int _initialLocalQueueSize) {
        super(PlantImpl.getSingleton().getInternalFacility() == null ? null : PlantImpl.getSingleton().getInternalFacility(),
                _initialOutboxSize, _initialLocalQueueSize);
        plantImpl = MPlantImpl.getSingleton();
    }

    public void initialize(final Reactor _reactor) throws Exception{
        super.initialize(_reactor);
        propertiesReference = new PropertiesReference(this.getFacility());
    }

    public void nameSet(final String _name) throws Exception {
        name = _name;
        plantMFacilityImpl = plantImpl.getInternalFacility().asFacilityImpl();
        tracePropertyChangesAReq().signal();
        String dependencyPrefix = MPlantImpl.dependencyPrefix(name);
        PropertiesReference plantProperties = plantMFacilityImpl.getPropertiesReference();
        ISMap<String> dependencies =
                plantProperties.getImmutable().subMap(dependencyPrefix);
        Iterator<String> dit = dependencies.keySet().iterator();
        while (dit.hasNext()) {
            String d = dit.next();
            String dependencyName = d.substring(dependencyPrefix.length());
            MFacilityImpl dependency = plantImpl.getMFacilityImpl(dependencyName);
            if (dependency == null)
                throw new DependencyNotPresentException(dependencyName);
            dependency.addCloseable(this);
        }
    }

    public AsyncRequest<Void> startFacilityAReq() {
        return new AsyncRequest<Void>(this.asReactor()) {
            AsyncRequest<Void> dis = this;

            AsyncResponseProcessor<Void> registerResponseProcessor =
                    new AsyncResponseProcessor<Void>() {
                        @Override
                        public void processAsyncResponse(Void _response) {
                            parentReactor.addCloseable(MFacilityImpl.this);
                            String activatorClassName = MPlant.getActivatorClassName(name);
                            if (activatorClassName == null)
                                dis.processAsyncResponse(null);
                            else {
                                send(activateAReq(activatorClassName), new AsyncResponseProcessor<String>() {
                                    @Override
                                    public void processAsyncResponse(final String _failure) throws Exception {
                                        if (_failure == null) {
                                            System.out.println("registered " + name);
                                            dis.processAsyncResponse(null);
                                            return;
                                        }
                                        close(false, _failure);
                                    }
                                });
                            }
                        }
                    };

            @Override
            public void processAsyncRequest() throws Exception {
                send(registerFacilityAReq(), registerResponseProcessor);
            }
        };
    }

    public MFacility asMFacility() {
        return (MFacility) asReactor();
    }

    public MFacility getFacility() {
        return (MFacility) getReactor();
    }

    public String getName() {
        return name;
    }

    public PropertiesReference getPropertiesReference() {
        return propertiesReference;
    }

    @Override
    public void close() throws Exception {
        close(false, null);
    }

    public void stop() throws Exception {
        close(true, null);
    }

    public void fail(final String reason) throws Exception {
        close(false, reason);
    }

    private void close(final boolean _stop, final String _reasonForFailure) throws Exception {
        if (_reasonForFailure != null && _stop)
            throw new IllegalArgumentException("can not both stop and fail");
        if (startedClosing()) {
            plantImpl.getInternalFacility().putPropertyAReq(MPlantImpl.failedKey(name), null, _reasonForFailure).
                    signal();
            plantImpl.getInternalFacility().putPropertyAReq(MPlantImpl.stoppedKey(name), null, true).
                    signal();
            return;
        }
        final MPlantImpl plantImpl = MPlantImpl.getSingleton();
        if ((plantImpl != null) &&
                plantImpl.getInternalFacility().asFacilityImpl() != this &&
                !plantImpl.getInternalFacility().asFacilityImpl().startedClosing()) {
            plantImpl.updateFacilityStatusAReq(null, name, _stop, _reasonForFailure).signal();
        }
        super.fail(_reasonForFailure);
    }

    private AsyncRequest<Void> registerFacilityAReq() {
        final MPlantImpl plantImpl = MPlantImpl.getSingleton();
        return plantImpl.updateFacilityStatusAReq(MFacilityImpl.this.asMFacility(), name, false, null);
    }

    /**
     * Returns the value of a property.
     *
     * @param propertyName The property name.
     * @return The property value, or null.
     */
    public Object getProperty(final String propertyName) {
        return propertiesReference.getImmutable().get(propertyName);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final Boolean _propertyValue) {
        return new UpdatePropertyTransaction(_propertyName, _propertyValue).
                applyAReq(propertiesReference);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final String _propertyValue) {
        return new UpdatePropertyTransaction(_propertyName, _propertyValue).
                applyAReq(propertiesReference);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final Boolean _expectedValue,
                                              final Boolean _propertyValue) {
        return new UpdatePropertyTransaction(_propertyName, _propertyValue, _expectedValue).
                applyAReq(propertiesReference);
    }

    public AsyncRequest<ISMap<String>> putPropertyAReq(final String _propertyName,
                                              final String _expectedValue,
                                              final String _propertyValue) {
        return new UpdatePropertyTransaction(_propertyName, _propertyValue, _expectedValue).
                applyAReq(propertiesReference);
    }

    protected ClassLoader getClassLoader() throws Exception {
        return getClass().getClassLoader();
    }

    public AsyncRequest<ClassLoader> getClassLoaderAReq() {
        return new AsyncBladeRequest<ClassLoader>() {
            @Override
            public void processAsyncRequest() throws Exception {
                processAsyncResponse(getClassLoader());
            }
        };
    }

    public AsyncRequest<String> activateAReq(final String _activatorClassName) {
        return new AsyncBladeRequest<String>() {
            @Override
            public void processAsyncRequest() throws Exception {
                setExceptionHandler(new ExceptionHandler<String>() {
                    @Override
                    public String processException(Exception e) throws Exception {
                        getLogger().error("activation exception, facility " + name, e);
                        return "activation exception, " + e;
                    }
                });
                final Class<?> initiatorClass = getClassLoader().loadClass(
                        _activatorClassName);
                final Constructor<?> constructor = initiatorClass.getConstructor(NonBlockingReactor.class);
                final Activator activator = (Activator) constructor.newInstance(asReactor());
                send(activator.startAReq(), this, null);
            }
        };
    }

    public AsyncRequest<Subscription<ImmutablePropertyChanges>> tracePropertyChangesAReq() {
        return new SubscribeAReq<ImmutablePropertyChanges>(propertiesReference.changeBus, asReactor()) {
            @Override
            protected void processContent(final ImmutablePropertyChanges _content)
                    throws Exception {
                SortedMap<String, PropertyChange> readOnlyChanges = _content.readOnlyChanges;
                final Iterator<PropertyChange> it = readOnlyChanges.values().iterator();
                while (it.hasNext()) {
                    final PropertyChange propertyChange = it.next();
                    String[] args = {
                            name,
                            propertyChange.name,
                            "" + propertyChange.oldValue,
                            "" + propertyChange.newValue
                    };
                    logger.info("\n    facility={}\n    key={}\n    old={}\n    new={}", args);
                }
            }
        };
    }

    public void facilityPoll() throws Exception {
        Iterator<Closeable> it = getCloseableSet().iterator();
        while (it.hasNext()) {
            Closeable closeable = it.next();
            if (!(closeable instanceof ReactorMtImpl))
                continue;
            ReactorMtImpl reactor = (ReactorMtImpl) closeable;
            reactor.reactorPoll();
        }
        reactorPoll();
    }
}
