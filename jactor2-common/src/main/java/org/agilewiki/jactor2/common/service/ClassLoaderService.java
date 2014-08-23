package org.agilewiki.jactor2.common.service;

import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.CPlantImpl;
import org.agilewiki.jactor2.common.Service;
import org.agilewiki.jactor2.core.reactors.BlockingReactor;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.Reactor;

public class ClassLoaderService extends Service {
    public static final String CLASS_LOADER_SERVICE_NAME = "classLoader";

    public static ClassLoaderService getClassLoaderService() throws Exception {
        return getClassLoaderService(CPlantImpl.getSingleton().getInternalFacility());
    }

    public static ClassLoaderService getClassLoaderService(final Reactor _reactor) throws Exception {
        Facility facility = CPlant.getFacility(_reactor);
        return (ClassLoaderService) facility.getBlade(CLASS_LOADER_SERVICE_NAME);
    }

    public static void register() throws Exception {
        new ClassLoaderService().registerSOp().call();
    }

    public final ClassLoader classLoader;

    public ClassLoaderService() throws Exception {
        this(new BlockingReactor());
    }

    public ClassLoaderService(BlockingReactor _reactor) {
        super(_reactor, CLASS_LOADER_SERVICE_NAME);
        classLoader = createClassLoader();
    }

    protected ClassLoader createClassLoader() {
        return getClass().getClassLoader();
    }
}
