package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.common.services.ClassLoaderService;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.core.blades.transmutable.TransmutableSortedMap;
import org.agilewiki.jactor2.core.reactors.Facility;
import org.agilewiki.jactor2.core.reactors.Reactor;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.core.requests.impl.RequestImpl;
import org.xeustechnologies.jcl.JarClassLoader;

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

    public JarClassLoader getJCL() throws Exception {
        return ClassLoaderService.getClassLoaderService(this).jcl;
    }

    public ClassLoader getClassLoader() throws Exception {
        JarClassLoader jcl = getJCL();
        if (jcl != null)
            return jcl;
        return ClassLoader.getSystemClassLoader();
    }

    public Class loadClass(String _className) throws Exception {
        return getClassLoader().loadClass(_className);
    }

    public SortedMap<String, WidgetFactory> getWidgetFactories() {
        return widgetFactories;
    }

    public Widget newWidget(final String _factoryName, Reactor _reactor) throws Exception {
        return newWidget(_factoryName, _reactor, null);
    }

    public Widget newWidget(final String _factoryName, Reactor _reactor, Widget _parentWidget) throws Exception {
        return widgetFactories.get(_factoryName).newWidget(_reactor, _parentWidget);
    }

    public SOp<Void> addWidgetFactorySOp(final WidgetFactory _widgetFactory) {
        final String _name = _widgetFactory.getName();
        if (_name == null) {
            throw new IllegalArgumentException("name may not be null");
        }
        if (_name.length() == 0) {
            throw new IllegalArgumentException("name may not be empty");
        }
        if (_name.contains(" ")) {
            throw new IllegalArgumentException("name may not contain spaces: "
                    + _name);
        }
        if (_name.contains(".")) {
            throw new IllegalArgumentException("name may not contain .: "
                    + _name);
        }
        return new SOp<Void>("addWidgetFactory", this) {
            @Override
            protected Void processSyncOperation(RequestImpl _requestImpl) throws Exception {
                if (widgetFactories.containsKey(_name)) {
                    throw new IllegalArgumentException("duplicate widget factory name");
                }
                widgetFactoriesTransmutable.put(_name, _widgetFactory);
                widgetFactories = widgetFactoriesTransmutable.createUnmodifiable();
                return null;
            }
        };
    }
}

