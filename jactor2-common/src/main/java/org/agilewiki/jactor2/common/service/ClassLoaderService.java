package org.agilewiki.jactor2.common.service;

import org.agilewiki.jactor2.common.CPlant;
import org.agilewiki.jactor2.common.CPlantImpl;
import org.agilewiki.jactor2.core.reactors.BlockingReactor;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.SOp;
import org.xeustechnologies.jcl.CompositeProxyClassLoader;
import org.xeustechnologies.jcl.JarClassLoader;

public class ClassLoaderService extends Service {
    public static final String CLASS_LOADER_SERVICE_NAME = "classLoader";

    public static ClassLoaderService getClassLoaderService() throws Exception {
        return getClassLoaderService(CPlantImpl.getSingleton().getInternalFacility());
    }

    public static ClassLoaderService getClassLoaderService(final Reactor _reactor) throws Exception {
        Facility facility = CPlant.getFacility(_reactor);
        return (ClassLoaderService) facility.getBlade(CLASS_LOADER_SERVICE_NAME);
    }

    public final JarClassLoader jcl;

    public final CompositeProxyClassLoader ccl;

    public ClassLoaderService() throws Exception {
        this(new BlockingReactor());
    }

    public ClassLoaderService(Reactor _reactor) {
        super(_reactor, CLASS_LOADER_SERVICE_NAME);
        jcl = new JarClassLoader();
        ccl = new CompositeProxyClassLoader();
        ccl.setOrder(12);
        jcl.addLoader(ccl);
    }
}
