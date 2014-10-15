package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;

import java.nio.ByteBuffer;

public class DurableFactory extends WidgetFactory {
    public DurableFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public DurableImpl newWidgetImpl(final WidgetImpl _parent,
                                     final ByteBuffer _byteBuffer)
            throws Exception {
        return new DurableImpl(this, (DurableImpl) _parent, _byteBuffer);
    }
}
