package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.durable.widgets.DurableFactory;

import java.nio.ByteBuffer;

public class IntFactory extends DurableFactory {

    public static final String FACTORY_NAME = "int";

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addWidgetFactorySOp(new IntFactory(_facility));
    }

    public static IntFactory getFactory(final CFacility _facility) {
        return (IntFactory) _facility.getWidgetFactory(FACTORY_NAME);
    }

    public IntFactory(CFacility _facility) {
        super(FACTORY_NAME, _facility);
    }

    public IntFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public IntImpl newWidgetImpl(final WidgetImpl _parent,
                                 final ByteBuffer _byteBuffer)
            throws Exception {
        return new IntImpl(this, (IntImpl) _parent, _byteBuffer);
    }
}
