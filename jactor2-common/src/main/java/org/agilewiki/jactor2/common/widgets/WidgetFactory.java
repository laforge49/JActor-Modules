package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;

public class WidgetFactory {
    public static String factoryKey(String facilityName, String widgetFactoryName) {
        return facilityName + "." + widgetFactoryName;
    }

    private final String name;
    private final CFacility facility;

    public WidgetFactory(final String _name, final CFacility _facility) {
        if (_name.contains("."))
            throw new IllegalArgumentException("name must not contain a .");
        name = _name;
        facility = _facility;
    }

    public String getName() {
        return name;
    }

    public CFacility getFacility() {
        return facility;
    }

    public String getFactoryKey() {
        return factoryKey(getFacility().name, name);
    }

    public WidgetImpl newWidgetImpl(final WidgetImpl _parent) throws Exception {
        return new WidgetImpl(this, _parent);
    }
}
