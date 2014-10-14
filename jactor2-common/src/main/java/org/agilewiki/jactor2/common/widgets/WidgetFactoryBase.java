package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;

public class WidgetFactoryBase implements WidgetFactory {
    public static String factoryKey(String facilityName, String widgetFactoryName) {
        return facilityName + "." + widgetFactoryName;
    }

    public final String name;
    public final CFacility facility;

    public WidgetFactoryBase(final String _name, final CFacility _facility) {
        if (_name.contains("."))
            throw new IllegalArgumentException("name must not contain a .");
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

    public WidgetImpl newWidgetImpl(final WidgetImpl _parent) throws Exception {
        return new WidgetImpl(this, _parent);
    }
}
