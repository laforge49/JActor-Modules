package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.common.services.ClassLoaderService;
import org.agilewiki.jactor2.common.widgets.InternalWidgetFactory;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.blades.transmutable.TransmutableSortedMap;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;
import org.xeustechnologies.jcl.JarClassLoader;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class CFacility extends Facility {
    private volatile SortedMap<String, InternalWidgetFactory> internalWidgetFactories = new TreeMap();
    protected TransmutableSortedMap<String, InternalWidgetFactory> internalWidgetFactoriesTransmutable = new TransmutableSortedMap();

    public CFacility(String _name) throws Exception {
        super(_name);
    }

    public CFacility(String _name, Facility _parentReactor) throws Exception {
        super(_name, _parentReactor);
    }

    public CFacility(String _name, int _initialOutboxSize, int _initialLocalQueueSize) throws Exception {
        super(_name, _initialOutboxSize, _initialLocalQueueSize);
    }

    public CFacility(String _name, Void _parentReactor, int _initialOutboxSize, int _initialLocalQueueSize) throws Exception {
        super(_name, _parentReactor, _initialOutboxSize, _initialLocalQueueSize);
    }

    public NamedBlade getNamedBlade(final String _bladeName) {
        return getNamedBlades().get(_bladeName);
    }

    public JarClassLoader getJCL() throws Exception {
        return ClassLoaderService.getClassLoaderService(this).jcl;
    }

    public ClassLoader getClassLoader() throws Exception {
        JarClassLoader jcl = getJCL();
        if (jcl != null)
            return jcl;
        return ClassLoader.getSystemClassLoader();
    }

    public Class loadClass(final String _className) throws Exception {
        return getClassLoader().loadClass(_className);
    }

    public SortedMap<String, InternalWidgetFactory> getInternalWidgetFactories() {
        return internalWidgetFactories;
    }

    public InternalWidgetFactory getInternalWidgetFactory(String _factoryKey) {
        if (!_factoryKey.contains("."))
            _factoryKey = name + "." + _factoryKey;
        return (InternalWidgetFactory) internalWidgetFactories.get(_factoryKey);
    }

    public InternalWidget newInternalWidget(final String _factoryKey,
                                            final InternalWidget _parentWidget,
                                            final ByteBuffer _byteBufffer) throws Exception {
        return ((InternalWidgetFactory) getInternalWidgetFactory(_factoryKey)).
                newInternalWidget(_parentWidget, _byteBufffer);
    }

    public SOp<Void> addInternalWidgetFactorySOp(final InternalWidgetFactory _widgetFactory) {
        final String factoryKey = _widgetFactory.getFactoryKey();
        if (factoryKey == null) {
            throw new IllegalArgumentException("factory key may not be null");
        }
        if (factoryKey.length() == 0) {
            throw new IllegalArgumentException("factory key may not be empty");
        }
        if (factoryKey.contains(" ")) {
            throw new IllegalArgumentException("factory key may not contain spaces: "
                    + factoryKey);
        }
        if (!factoryKey.startsWith(name + ".")) {
            throw new IllegalArgumentException("factory key must start with "
                    + factoryKey);
        }
        return new SOp<Void>("addInternalWidgetFactory", this) {
            @Override
            protected Void processSyncOperation(RequestImpl _requestImpl) throws Exception {
                if (internalWidgetFactories.containsKey(factoryKey)) {
                    throw new IllegalArgumentException("duplicate widget factory key");
                }
                internalWidgetFactoriesTransmutable.put(factoryKey, _widgetFactory);
                internalWidgetFactories = internalWidgetFactoriesTransmutable.createUnmodifiable();
                return null;
            }
        };
    }

    public SOp<Void> addInternalWidgetFactorySOp(final Collection<InternalWidgetFactory> _widgetFactories) {
        return new SOp<Void>("addInternalWidgetFactory", this) {
            @Override
            protected Void processSyncOperation(RequestImpl _requestImpl) throws Exception {
                Iterator<InternalWidgetFactory> it = _widgetFactories.iterator();
                while (it.hasNext()) {
                    InternalWidgetFactory widgetFactory = it.next();
                    final String factoryKey = widgetFactory.getFactoryKey();
                    if (factoryKey == null) {
                        throw new IllegalArgumentException("factory key may not be null");
                    }
                    if (factoryKey.length() == 0) {
                        throw new IllegalArgumentException("factory key may not be empty");
                    }
                    if (factoryKey.contains(" ")) {
                        throw new IllegalArgumentException("factory key may not contain spaces: "
                                + factoryKey);
                    }
                    if (!factoryKey.startsWith(name + ".")) {
                        throw new IllegalArgumentException("factory key must start with "
                                + name);
                    }
                    if (internalWidgetFactories.containsKey(factoryKey)) {
                        throw new IllegalArgumentException("duplicate widget factory key");
                    }
                    internalWidgetFactoriesTransmutable.put(factoryKey, widgetFactory);
                }
                internalWidgetFactories = internalWidgetFactoriesTransmutable.createUnmodifiable();
                return null;
            }
        };
    }
}

