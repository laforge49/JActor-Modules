package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.common.services.ClassLoaderService;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
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
    private volatile SortedMap<String, WidgetFactory> widgetFactories = new TreeMap();
    protected TransmutableSortedMap<String, WidgetFactory> widgetFactoriesTransmutable = new TransmutableSortedMap();

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

    public SortedMap<String, WidgetFactory> getWidgetFactories() {
        return widgetFactories;
    }

    public WidgetFactory getWidgetFactory(String _factoryKey) {
        if (!_factoryKey.contains("."))
            _factoryKey = name + "." + _factoryKey;
        return (WidgetFactory) widgetFactories.get(_factoryKey);
    }

    public Widget newWidget(final String _factoryKey,
                            final Widget _parentWidget,
                            final ByteBuffer _byteBufffer) {
        return getWidgetFactory(_factoryKey).
                newWidget(_parentWidget, _byteBufffer);
    }

    public Widget newWidget(final String _factoryKey, final ByteBuffer _byteBufffer) {
        return getWidgetFactory(_factoryKey).
                newWidget(null, _byteBufffer);
    }

    public SOp<Void> addWidgetFactorySOp(final WidgetFactory _widgetFactory) {
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
        return new SOp<Void>("addWidgetFactory", this) {
            @Override
            protected Void processSyncOperation(RequestImpl _requestImpl) throws Exception {
                if (widgetFactories.containsKey(factoryKey)) {
                    throw new IllegalArgumentException("duplicate widget factory key");
                }
                widgetFactoriesTransmutable.put(factoryKey, _widgetFactory);
                widgetFactories = widgetFactoriesTransmutable.createUnmodifiable();
                return null;
            }
        };
    }

    public SOp<Void> addWidgetFactorySOp(final Collection<WidgetFactory> _widgetFactories) {
        return new SOp<Void>("addWidgetFactory", this) {
            @Override
            protected Void processSyncOperation(RequestImpl _requestImpl) throws Exception {
                Iterator<WidgetFactory> it = _widgetFactories.iterator();
                while (it.hasNext()) {
                    WidgetFactory widgetFactory = it.next();
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
                    if (widgetFactories.containsKey(factoryKey)) {
                        throw new IllegalArgumentException("duplicate widget factory key");
                    }
                    widgetFactoriesTransmutable.put(factoryKey, widgetFactory);
                }
                widgetFactories = widgetFactoriesTransmutable.createUnmodifiable();
                return null;
            }
        };
    }
}

