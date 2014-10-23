package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.InternalWidgetFactory;
import org.agilewiki.jactor2.core.requests.SOp;

import java.nio.ByteBuffer;

public class IntFactory extends InternalWidgetFactory {

    public static final String FACTORY_NAME = "int";

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addInternalWidgetFactorySOp(new IntFactory(_facility));
    }

    public static IntFactory getFactory(final CFacility _facility) {
        return (IntFactory) _facility.getInternalWidgetFactory(FACTORY_NAME);
    }

    public IntFactory(CFacility _facility) {
        super(FACTORY_NAME, _facility);
    }

    public IntFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public IntImpl newInternalWidget(final InternalWidget _parent,
                                     final ByteBuffer _byteBuffer) {
        return new IntImpl(this, (IntImpl) _parent, _byteBuffer);
    }
}
