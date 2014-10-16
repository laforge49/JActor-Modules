package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.durable.widgets.DurableFactory;

import java.nio.ByteBuffer;

public class IntFactory extends DurableFactory {
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
