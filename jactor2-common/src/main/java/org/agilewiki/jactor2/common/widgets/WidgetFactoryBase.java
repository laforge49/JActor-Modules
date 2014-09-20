package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.reactors.Reactor;

public class WidgetFactoryBase extends BladeBase implements WidgetFactory, NamedBlade {
    public static String factoryKey(String facilityName, String widgetFactoryName) {
        return facilityName + "." + widgetFactoryName;
    }

    public final String name;

    public WidgetFactoryBase(String _name, CFacility _cFacility) {
        name = _name;
        super._initialize(_cFacility);
    }

    @Override
    public String getName() {
        return name;
    }

    public CFacility getCFacility() {
        return (CFacility) getReactor();
    }

    @Override
    public String getFactoryKey() {
        return factoryKey(getCFacility().name, name);
    }

    public Widget newWidget(Reactor _reactor, Widget _parentWidget) throws Exception {
        return new WidgetBase(_reactor, _parentWidget);
    }

    protected class WidgetBase extends BladeBase implements Widget {
        private final Widget widgetParent;

        WidgetBase(Reactor _reactor, Widget _widgetParent) {
            widgetParent = _widgetParent;
            _initialize(_reactor);
        }

        @Override
        public WidgetFactory getWidgetFactory() {
            return WidgetFactoryBase.this;
        }

        @Override
        public Widget getParentWidget() {
            return widgetParent;
        }
    }
}
