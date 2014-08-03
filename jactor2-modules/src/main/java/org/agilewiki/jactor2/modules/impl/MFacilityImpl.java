package org.agilewiki.jactor2.modules.impl;

import org.agilewiki.jactor2.core.blades.ismTransactions.ISMReference;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMUpdateTransaction;
import org.agilewiki.jactor2.core.blades.ismTransactions.ImmutableChange;
import org.agilewiki.jactor2.core.blades.ismTransactions.ImmutableChanges;
import org.agilewiki.jactor2.core.blades.pubSub.SubscribeAOp;
import org.agilewiki.jactor2.core.blades.pubSub.Subscription;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMap;
import org.agilewiki.jactor2.core.closeable.Closeable;
import org.agilewiki.jactor2.core.impl.mtReactors.NonBlockingReactorMtImpl;
import org.agilewiki.jactor2.core.impl.mtReactors.ReactorMtImpl;
import org.agilewiki.jactor2.core.plant.impl.PlantImpl;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.ExceptionHandler;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.modules.Activator;
import org.agilewiki.jactor2.modules.DependencyNotPresentException;
import org.agilewiki.jactor2.modules.MFacility;
import org.agilewiki.jactor2.modules.MPlant;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.SortedMap;

public class MFacilityImpl extends NonBlockingReactorMtImpl {
    protected ISMReference<String> ismReference;

    private MPlantImpl plantImpl;

    private MFacilityImpl plantMFacilityImpl;

    private String name;

    public MFacilityImpl(final int _initialOutboxSize, final int _initialLocalQueueSize) {
        super(PlantImpl.getSingleton().getInternalFacility() == null ? null : PlantImpl.getSingleton().getInternalFacility(),
                _initialOutboxSize, _initialLocalQueueSize);
        plantImpl = MPlantImpl.getSingleton();
    }

    public void initialize(final Reactor _reactor) throws Exception {
        super.initialize(_reactor);
        ismReference = new ISMReference<String>(this.getFacility());
    }

    public void nameSet(final String _name) throws Exception {
        name = _name;
        plantMFacilityImpl = plantImpl.getInternalFacility().asFacilityImpl();
        tracePropertyChangesAOp().signal();
        String dependencyPrefix = MPlantImpl.dependencyPrefix(name);
        ISMReference<String> plantProperties = plantMFacilityImpl.getISMReference();
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

    public AOp<Void> startFacilityAOp() {
        return new AOp<Void>("startFacility", asReactor()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                AsyncResponseProcessor<Void> registerResponseProcessor =
                        new AsyncResponseProcessor<Void>() {
                            @Override
                            public void processAsyncResponse(Void _response) throws Exception {
                                parentReactor.addCloseable(MFacilityImpl.this);
                                String activatorClassName = MPlant.getActivatorClassName(name);
                                if (activatorClassName == null)
                                    _asyncResponseProcessor.processAsyncResponse(null);
                                else {
                                    _asyncRequestImpl.send(activateAOp(activatorClassName), new AsyncResponseProcessor<String>() {
                                        @Override
                                        public void processAsyncResponse(final String _failure) throws Exception {
                                            if (_failure == null) {
                                                System.out.println("registered " + name);
                                                _asyncResponseProcessor.processAsyncResponse(null);
                                                return;
                                            }
                                            close(false, _failure);
                                        }
                                    });
                                }
                            }
                        };

                _asyncRequestImpl.send(registerFacilityAOp(), registerResponseProcessor);
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

    public ISMReference<String> getISMReference() {
        return ismReference;
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
            plantImpl.getInternalFacility().putPropertyAOp(MPlantImpl.failedKey(name), null, _reasonForFailure).
                    signal();
            plantImpl.getInternalFacility().putPropertyAOp(MPlantImpl.stoppedKey(name), null, "true").
                    signal();
            return;
        }
        final MPlantImpl plantImpl = MPlantImpl.getSingleton();
        if ((plantImpl != null) &&
                plantImpl.getInternalFacility().asFacilityImpl() != this &&
                !plantImpl.getInternalFacility().asFacilityImpl().startedClosing()) {
            plantImpl.updateFacilityStatusAOp(null, name, _stop, _reasonForFailure).signal();
        }
        super.fail(_reasonForFailure);
    }

    private AOp<Void> registerFacilityAOp() {
        final MPlantImpl plantImpl = MPlantImpl.getSingleton();
        return plantImpl.updateFacilityStatusAOp(MFacilityImpl.this.asMFacility(), name, false, null);
    }

    /**
     * Returns the value of a property.
     *
     * @param propertyName The property name.
     * @return The property value, or null.
     */
    public Object getProperty(final String propertyName) {
        return ismReference.getImmutable().get(propertyName);
    }

    public AOp<ISMap<String>> putPropertyAOp(final String _propertyName,
                                             final String _propertyValue) {
        return new ISMUpdateTransaction<String>(_propertyName, _propertyValue).
                applyAOp(ismReference);
    }

    public AOp<ISMap<String>> putPropertyAOp(final String _propertyName,
                                             final String _expectedValue,
                                             final String _propertyValue) {
        return new ISMUpdateTransaction<String>(_propertyName, _propertyValue, _expectedValue).
                applyAOp(ismReference);
    }

    protected ClassLoader getClassLoader() throws Exception {
        return getClass().getClassLoader();
    }

    public AOp<ClassLoader> getClassLoaderAOp() {
        return new AOp<ClassLoader>("getClassLoader", getReactor()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                              final AsyncResponseProcessor<ClassLoader> _asyncResponseProcessor)
                    throws Exception {
                _asyncResponseProcessor.processAsyncResponse(getClassLoader());
            }
        };
    }

    public AOp<String> activateAOp(final String _activatorClassName) {
        return new AOp<String>("activate", getReactor()) {
            @Override
            public void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                            final AsyncResponseProcessor<String> _asyncResponseProcessor)
                    throws Exception {
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
                _asyncRequestImpl.send(activator.startAOp(), _asyncResponseProcessor, null);
            }
        };
    }

    public AOp<Subscription<ImmutableChanges<String>>> tracePropertyChangesAOp() {
        return new SubscribeAOp<ImmutableChanges<String>>(ismReference.changeBus, asReactor()) {
            @Override
            protected void processContent(final ImmutableChanges<String> _content)
                    throws Exception {
                SortedMap<String, ImmutableChange<String>> readOnlyChanges = _content.readOnlyChanges;
                final Iterator<ImmutableChange<String>> it = readOnlyChanges.values().iterator();
                while (it.hasNext()) {
                    final ImmutableChange<String> propertyChange = it.next();
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
