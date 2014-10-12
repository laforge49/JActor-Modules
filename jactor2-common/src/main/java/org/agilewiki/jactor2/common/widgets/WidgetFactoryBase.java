package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.core.blades.BladeBase;
import org.agilewiki.jactor2.core.blades.NamedBlade;
import org.agilewiki.jactor2.core.reactors.Reactor;

public class WidgetFactoryBase implements WidgetFactory {
    public static String factoryKey(String facilityName, String widgetFactoryName) {
        return facilityName + "." + widgetFactoryName;
    }

    public final String name;
    public final CFacility facility;

    public WidgetFactoryBase(final String _name, final CFacility _facility) {
        name = _name;
        facility = _facility;
    }

    @Override
    public String getName() {
        return name;
    }

    public CFacility getFacility() {
        return facility;
    }

    @Override
    public String getFactoryKey() {
        return factoryKey(getFacility().name, name);
    }

    public Widget newWidget(Widget _parentWidget) throws Exception {
        return new WidgetBase(_parentWidget);
    }

    protected class WidgetBase implements Widget {
        private final Widget widgetParent;

        WidgetBase(Widget _widgetParent) {
            widgetParent = _widgetParent;
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
