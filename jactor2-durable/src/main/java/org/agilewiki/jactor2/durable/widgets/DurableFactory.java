package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.core.requests.SOp;

import java.nio.ByteBuffer;

public class DurableFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "null";

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addInternalWidgetFactorySOp(new DurableFactory(_facility));
    }

    public static DurableFactory getFactory(final CFacility _facility) {
        return (DurableFactory) _facility.getInternalWidgetFactory(FACTORY_NAME);
    }

    public DurableFactory(CFacility _facility) {
        super(FACTORY_NAME, _facility);
    }

    public DurableFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public DurableImpl newInternalWidget(final InternalWidget _parent,
                                         final ByteBuffer _byteBuffer)
            throws Exception {
        return new DurableImpl(this, (DurableImpl) _parent, _byteBuffer);
    }
}
