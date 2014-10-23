package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.core.requests.SOp;

import java.nio.ByteBuffer;

public class InternalWidgetFactory {

    public static final String FACTORY_NAME = "null";

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addInternalWidgetFactorySOp(new InternalWidgetFactory(_facility));
    }

    public static InternalWidgetFactory getFactory(final CFacility _facility) {
        return (InternalWidgetFactory) _facility.getInternalWidgetFactory(FACTORY_NAME);
    }

    public static String factoryKey(String facilityName, String widgetFactoryName) {
        return facilityName + "." + widgetFactoryName;
    }

    private final String name;
    private final CFacility facility;

    public InternalWidgetFactory(CFacility _facility) {
        this(FACTORY_NAME, _facility);
    }

    public InternalWidgetFactory(final String _name, final CFacility _facility) {
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

    public InternalWidget newInternalWidget(final InternalWidget _parent,
                                            ByteBuffer _byteBuffer) {
        return new WidgetImpl(this, _parent, _byteBuffer);
    }
}
