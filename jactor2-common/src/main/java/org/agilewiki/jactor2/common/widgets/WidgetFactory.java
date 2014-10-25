package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.core.requests.SOp;

import java.nio.ByteBuffer;

public class WidgetFactory {

    public static final String FACTORY_NAME = "null";

    public static WidgetImpl._Widget newWidget(final CFacility _facility) {
        return new WidgetImpl(_facility, null).asWidget();
    }

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addWidgetFactorySOp(new WidgetFactory(_facility));
    }

    public static WidgetFactory getFactory(final CFacility _facility) {
        return _facility.getWidgetFactory(FACTORY_NAME);
    }

    public static String factoryKey(final CFacility _facility) {
        return getFactory(_facility).getFactoryKey();
    }

    private final String name;
    private final CFacility facility;

    public WidgetFactory(CFacility _facility) {
        this(FACTORY_NAME, _facility);
    }

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
        return getFacility().name + "." + name;
    }

    public InternalWidget newInternalWidget(final InternalWidget _parent,
                                            ByteBuffer _byteBuffer) {
        return new WidgetImpl(this, _parent, _byteBuffer);
    }
}
