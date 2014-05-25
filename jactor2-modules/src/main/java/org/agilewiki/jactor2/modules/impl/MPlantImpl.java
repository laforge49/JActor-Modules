package org.agilewiki.jactor2.modules.impl;

import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.blades.filters.PrefixFilter;
import org.agilewiki.jactor2.core.blades.ismTransactions.*;
import org.agilewiki.jactor2.core.blades.pubSub.RequestBus;
import org.agilewiki.jactor2.core.blades.pubSub.SubscribeAReq;
import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.impl.mtPlant.PlantConfiguration;
import org.agilewiki.jactor2.core.impl.mtPlant.PlantMtImpl;
import org.agilewiki.jactor2.core.plant.PlantImpl;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.ExceptionHandler;
import org.agilewiki.jactor2.modules.MFacility;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;

public class MPlantImpl extends PlantMtImpl {

    public static final String CORE_PREFIX = "core.";

    public static final String FACILITY_PREFIX = "facility_";

    public static final String FACILITY_DEPENDENCY_INFIX = CORE_PREFIX+"dependency_";

    public static String dependencyPrefix(final String _facilityName) {
        return FACILITY_PREFIX+_facilityName+"~"+FACILITY_DEPENDENCY_INFIX;
    }

    public static final String FACILITY_INITIAL_LOCAL_MESSAGE_QUEUE_SIZE_POSTFIX =
            CORE_PREFIX+"initialLocalMessageQueueSize";

    public static String initialLocalMessageQueueSizeKey(final String _facilityName) {
        return FACILITY_PREFIX+_facilityName+"~"+FACILITY_INITIAL_LOCAL_MESSAGE_QUEUE_SIZE_POSTFIX;
    }

    public static final String FACILITY_INITIAL_BUFFER_SIZE_POSTFIX = CORE_PREFIX+"initialBufferSize";

    public static String initialBufferSizeKey(final String _facilityName) {
        return FACILITY_PREFIX+_facilityName+"~"+FACILITY_INITIAL_BUFFER_SIZE_POSTFIX;
    }

    public static final String FACILITY_ACTIVATOR_POSTFIX = CORE_PREFIX+"activator";

    public static String activatorKey(final String _facilityName) {
        return FACILITY_PREFIX+_facilityName+"~"+FACILITY_ACTIVATOR_POSTFIX;
    }

    public static String FACILITY_AUTO_START_POSTFIX = CORE_PREFIX+"autoStart";

    public static String autoStartKey(final String _facilityName) {
        return FACILITY_PREFIX+_facilityName+"~"+FACILITY_AUTO_START_POSTFIX;
    }

    public static String FACILITY_FAILED_POSTFIX = CORE_PREFIX+"failed";

    public static String failedKey(final String _facilityName) {
        return FACILITY_PREFIX+_facilityName+"~"+FACILITY_FAILED_POSTFIX;
    }

    public static String FACILITY_STOPPED_POSTFIX = CORE_PREFIX+"stopped";

    public static String stoppedKey(final String _facilityName) {
        return FACILITY_PREFIX+_facilityName+"~"+FACILITY_STOPPED_POSTFIX;
    }

    public static MPlantImpl getSingleton() {
        return (MPlantImpl) PlantImpl.getSingleton();
    }

    private final ISMReference<String> propertiesReference;

    public MPlantImpl() throws Exception {
        this(new PlantConfiguration());
    }

    public MPlantImpl(final int _threadCount) throws Exception {
        this(new PlantConfiguration(_threadCount));
    }

    public MPlantImpl(final PlantConfiguration _plantConfiguration) throws Exception {
        super(_plantConfiguration);
        propertiesReference = getInternalFacility().getISMReference();
        validate();
        changes();
        int reactorPollMillis = _plantConfiguration.getRecovery().getReactorPollMillis();
        _plantConfiguration.getPlantScheduler().scheduleAtFixedRate(plantPoll(),
                reactorPollMillis);
    }

    public AsyncRequest<Void> updateFacilityStatusAReq(final MFacility _Mfacility,
                                                        final String _facilityName,
                                                        final boolean _stop,
                                                        final String _reasonForFailure) {
        System.out.println(">>>>>>>>>>>>>>>>> "+_facilityName);
        final String stop = _stop ? "true" : null;
        final MFacility internalMFacility = getInternalFacility();
        return new AsyncRequest<Void>(internalMFacility) {
            AsyncRequest<Void> dis = this;

            AsyncResponseProcessor<Void> transactionResponseProcessor = new AsyncResponseProcessor<Void>() {
                @Override
                public void processAsyncResponse(Void _response) {
                    if (_Mfacility != null)
                        internalMFacility.registerBlade(_Mfacility, internalMFacility);
                    ISMap<String> facilityProperties =
                            propertiesReference.getImmutable().subMap(FACILITY_PREFIX);
                    Iterator<String> kit = facilityProperties.keySet().iterator();
                    String postfix = "~"+FACILITY_DEPENDENCY_INFIX + _facilityName;
                    while (kit.hasNext()) {
                        String pk = kit.next();
                        if (!pk.endsWith(postfix))
                            continue;
                        String dependentName = pk.substring(FACILITY_PREFIX.length(), pk.length()-postfix.length());
                        autoStartAReq(dependentName).signal();
                    }
                    dis.processAsyncResponse(null);
                }
            };

            @Override
            public void processAsyncRequest() {
                if (_Mfacility == null) {
                    internalMFacility.unregisterBlade(_facilityName, internalMFacility);
                } else if (internalMFacility.isRegisteredBlade(_facilityName))
                    throw new IllegalStateException("Facility already registered: " + _facilityName);
                final ISMReference<String> propertiesReference = internalMFacility.getISMReference();
                ISMUpdateTransaction<String> t0 = new ISMUpdateTransaction<String>(stoppedKey(_facilityName), stop);
                ISMUpdateTransaction<String> t1 = new ISMUpdateTransaction<String>(failedKey(_facilityName), _reasonForFailure, t0);
                send(t1.applyAReq(propertiesReference), transactionResponseProcessor, null);
            }
        };
    }

    private void validate() throws Exception {
        RequestBus<ImmutableChanges<String>> validationBus = propertiesReference.validationBus;
        new SubscribeAReq<ImmutableChanges<String>>(
                validationBus,
                getInternalFacility()) {
            protected void processContent(final ImmutableChanges<String> _content)
                    throws Exception {
                SortedMap<String, ImmutableChange<String>> readOnlyChanges = _content.readOnlyChanges;
                ImmutableChange<String> pc;
                final Iterator<ImmutableChange<String>> it = readOnlyChanges.values().iterator();
                while (it.hasNext()) {
                    pc = it.next();
                    String key = pc.name;
                    Object newValue = pc.newValue;
                    if (key.startsWith(FACILITY_PREFIX)) {
                        String name1 = key.substring(FACILITY_PREFIX.length());
                        int i = name1.indexOf('~');
                        if (i == -1)
                            throw new UnsupportedOperationException("undeliminated facility");
                        String name2 = name1.substring(i + 1);
                        name1 = name1.substring(0, i);
                        MFacilityImpl facility0 = getMFacilityImpl(name1);
                        if (name2.startsWith(FACILITY_DEPENDENCY_INFIX)) {
                            if (facility0 != null) {
                                throw new IllegalStateException(
                                        "the dependency properties can not change while a facility is running ");
                            }
                            name2 = name2.substring(FACILITY_DEPENDENCY_INFIX.length());
                            if (PLANT_INTERNAL_FACILITY_NAME.equals(name1))
                                throw new UnsupportedOperationException("a plant can not have a dependency");
                            if (hasDependency(name2, key))
                                throw new IllegalArgumentException(
                                        "Would create a dependency cycle.");
                        } else if (name2.equals(FACILITY_INITIAL_LOCAL_MESSAGE_QUEUE_SIZE_POSTFIX)) {
                            if (facility0 != null) {
                                throw new IllegalStateException(
                                        "the initial local message queue size property can not change while a facility is running ");
                            }
                            if (PLANT_INTERNAL_FACILITY_NAME.equals(name1))
                                throw new UnsupportedOperationException(
                                        "a plant can not have an initial local message queue size property");
                            if (newValue != null && !(newValue instanceof Integer))
                                throw new IllegalArgumentException(
                                        "the initial local message queue size property value must be an Integer");
                        } else if (name2.equals(FACILITY_INITIAL_BUFFER_SIZE_POSTFIX)) {
                            if (facility0 != null) {
                                throw new IllegalStateException(
                                        "the initial buffer size property can not change while a facility is running ");
                            }
                            if (PLANT_INTERNAL_FACILITY_NAME.equals(name1))
                                throw new UnsupportedOperationException(
                                        "a plant can not have an initial buffer size property");
                            if (newValue != null && !(newValue instanceof Integer))
                                throw new IllegalArgumentException(
                                        "the initial buffer size property value must be an Integer");
                        } else if (name2.equals(FACILITY_ACTIVATOR_POSTFIX)) {
                            if (facility0 != null) {
                                throw new IllegalStateException(
                                        "the activator property can not change while a facility is running ");
                            }
                            if (PLANT_INTERNAL_FACILITY_NAME.equals(name1))
                                throw new UnsupportedOperationException(
                                        "a plant can not have an activator property");
                            if (newValue != null && !(newValue instanceof String))
                                throw new IllegalArgumentException(
                                        "the activator property value must be a String");
                        }
                    }
                }
            }
        }.signal();
    }

    public void changes() throws Exception {
        RequestBus<ImmutableChanges<String>> changeBus = propertiesReference.changeBus;
        new SubscribeAReq<ImmutableChanges<String>>(
                changeBus,
                getInternalFacility()) {
            protected void processContent(final ImmutableChanges<String> _content)
                    throws Exception {
                SortedMap<String, ImmutableChange<String>> readOnlyChanges = _content.readOnlyChanges;
                final Iterator<ImmutableChange<String>> it = readOnlyChanges.values().iterator();
                while (it.hasNext()) {
                    ImmutableChange<String> pc = it.next();
                    String key = pc.name;
                    Object newValue = pc.newValue;
                    if (key.startsWith(FACILITY_PREFIX)) {
                        String name1 = key.substring(FACILITY_PREFIX.length());
                        int i = name1.indexOf('~');
                        if (i == -1)
                            throw new UnsupportedOperationException("undeliminated facility");
                        String name2 = name1.substring(i + 1);
                        name1 = name1.substring(0, i);
                        if (name2.startsWith(FACILITY_AUTO_START_POSTFIX)) {
                            if (newValue != null)
                                autoStartAReq(name1).signal();
                        } else if (name2.startsWith(FACILITY_FAILED_POSTFIX)) {
                            if (newValue == null)
                                autoStartAReq(name1).signal();
                        } else if (name2.startsWith(FACILITY_STOPPED_POSTFIX)) {
                            if (newValue == null)
                                autoStartAReq(name1).signal();
                        }
                    }
                }
            }
        }.signal();
    }

    @Override
    public MFacility getInternalFacility() {
        return (MFacility) super.getInternalFacility();
    }

    public Object getProperty(final String propertyName) {
        return getInternalFacility().getProperty(propertyName);
    }

    protected MFacility createInternalFacility() throws Exception {
        return new MFacility(PLANT_INTERNAL_FACILITY_NAME, null,
                getPlantConfiguration().getInitialBufferSize(),
                getPlantConfiguration().getInitialLocalMessageQueueSize());
    }

    private Runnable plantPoll() {
        return new Runnable() {
            public void run() {
                try {
                    getInternalFacility().asFacilityImpl().facilityPoll();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        };
    }

    private AsyncRequest<String> autoStartAReq(final String _facilityName) {
        return new AsyncRequest<String>(getInternalFacility()) {
            @Override
            public void processAsyncRequest() throws Exception {
                if (getMFacilityImpl(_facilityName) != null) {
                    processAsyncResponse(null);
                    return;
                }
                if (!isAutoStart(_facilityName)) {
                    processAsyncResponse(null);
                    return;
                }
                if (getFailed(_facilityName) != null) {
                    processAsyncResponse(null);
                    return;
                }
                if (isStopped(_facilityName)) {
                    processAsyncResponse(null);
                    return;
                }
                String dependencyPrefix = dependencyPrefix(_facilityName);
                ISMap<String> dependencies =
                        propertiesReference.getImmutable().subMap(dependencyPrefix);
                Iterator<String> dit = dependencies.keySet().iterator();
                while (dit.hasNext()) {
                    String d = dit.next();
                    String dependencyName = d.substring(dependencyPrefix.length());
                    if (getMFacilityImpl(dependencyName) == null)
                        processAsyncResponse(null);
                }
                setExceptionHandler(new ExceptionHandler<String>() {
                    @Override
                    public String processException(Exception e) throws Exception {
                        if (e instanceof ReactorClosedException)
                            return null;
                        else
                            return "create facility exception: " + e;
                    }
                });
                send(MFacility.createMFacilityAReq(_facilityName), this, null);
            }
        };
    }

    public void stopFacility(final String _facilityName) throws Exception {
        MFacilityImpl facility = getMFacilityImpl(_facilityName);
        if (facility == null) {
            getInternalFacility().putPropertyAReq(stoppedKey(_facilityName), "true").signal();
            return;
        }
        facility.stop();
    }

    public void failFacility(final String _facilityName, final String reason) throws Exception {
        MFacilityImpl facility = getMFacilityImpl(_facilityName);
        if (facility == null) {
            getInternalFacility().putPropertyAReq(failedKey(_facilityName), reason).signal();
            return;
        }
        facility.fail(reason);
    }

    public AsyncRequest<Void> dependencyPropertyAReq(final String _dependentName, final String _dependencyName) {
        return new AsyncRequest<Void>(getInternalFacility()) {

            AsyncResponseProcessor<Void> dis = this;

            String dependencyPropertyName;

            @Override
            public void processAsyncRequest() throws Exception {
                final String name = _dependencyName;
                if (_dependencyName == null) {
                    throw new IllegalArgumentException(
                            "the dependency name may not be null");
                }
                if (PLANT_INTERNAL_FACILITY_NAME.equals(_dependencyName))
                    dis.processAsyncResponse(null);
                if (PLANT_INTERNAL_FACILITY_NAME.equals(_dependentName))
                    throw new IllegalArgumentException("Plant may not have a dependency");
                dependencyPropertyName = dependencyPrefix(_dependentName) + name;
                if (getProperty(dependencyPropertyName) != null) {
                    throw new IllegalStateException(
                            "the dependency was already present");
                }
                if (hasDependency(_dependencyName, _dependentName))
                    throw new IllegalArgumentException(
                            "this would create a cyclic dependency");
                send(getInternalFacility().putPropertyAReq(dependencyPropertyName, "true"), dis, null);
            }
        };
    }

    public boolean hasDependency(final String _dependentName, final String _dependencyName) throws Exception {
        String prefix = FACILITY_PREFIX + _dependentName + "~" + FACILITY_DEPENDENCY_INFIX;
        if (getProperty(prefix + _dependencyName) != null)
            return true;
        final ISMap<String> immutableProperties = propertiesReference.getImmutable();
        final ISMap<String> subMap = immutableProperties.subMap(prefix);
        final Collection<String> keys = subMap.keySet();
        if (keys.size() == 0)
            return false;
        final Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            final String key = it.next();
            String nm = key.substring(prefix.length());
            if (hasDependency(nm, _dependencyName))
                return true;
        }
        return false;
    }

    public AsyncRequest<ISMap<String>> initialLocalMessageQueueSizePropertyAReq(final String _facilityName,
                                                                                      final Integer _value) {
        return getInternalFacility().putPropertyAReq(initialLocalMessageQueueSizeKey(_facilityName),
                new Integer(_value).toString());
    }

    public AsyncRequest<ISMap<String>> initialBufferSizePropertyAReq(final String _facilityName,
                                                                           final Integer _value) {
        return getInternalFacility().putPropertyAReq(initialBufferSizeKey(_facilityName),
                new Integer(_value).toString());
    }

    public AsyncRequest<ISMap<String>> activatorPropertyAReq(final String _facilityName,
                                                                   final String _className) {
        return getInternalFacility().putPropertyAReq(activatorKey(_facilityName), _className);
    }

    public String getActivatorClassName(final String _facilityName) {
        return (String) getProperty(activatorKey(_facilityName));
    }

    public MFacilityImpl getMFacilityImpl(String name) {
        NamedBlade blade = getInternalFacility().getBlade(name);
        if (blade == null)
            return null;
        if (!(blade instanceof Facility))
            throw new IllegalArgumentException("not the name of an MFacility");
        MFacility MFacility = (MFacility) blade;
        return MFacility.asFacilityImpl();
    }

    public AsyncRequest<ISMap<String>> autoStartAReq(final String _facilityName, final boolean _newValue) {
        final String newValue = _newValue ? "true" : null;
        return getInternalFacility().putPropertyAReq(autoStartKey(_facilityName), newValue);
    }

    public boolean isAutoStart(String name) {
        return getProperty(autoStartKey(name)) != null;
    }

    public AsyncRequest<ISMap<String>> failedAReq(final String _facilityName, final String _newValue) {
        return getInternalFacility().putPropertyAReq(failedKey(_facilityName), _newValue);
    }

    public Object getFailed(String name) {
        return getProperty(failedKey(name));
    }

    public AsyncRequest<ISMap<String>> stoppedAReq(final String _facilityName, final boolean _newValue) {
        final String newValue = _newValue ? "true" : null;
        return getInternalFacility().putPropertyAReq(stoppedKey(_facilityName), newValue);
    }

    public boolean isStopped(String name) {
        return getProperty(stoppedKey(name)) != null;
    }

    public AsyncRequest<ISMap<String>> purgeFacilitySReq(final String _facilityName) {
        String prefix = FACILITY_PREFIX + _facilityName + ".";
        PrefixFilter filter = new PrefixFilter(prefix);
        return new ISMRemoveTransaction<String>(filter).applyAReq(propertiesReference);
    }
}
