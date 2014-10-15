package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;

public class DurableFactory extends WidgetFactory<UnmodifiableByteBufferFactory> {
    public DurableFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public DurableImpl newWidgetImpl(final WidgetImpl<UnmodifiableByteBufferFactory> _parent,
                                     final UnmodifiableByteBufferFactory _unmodifiable)
            throws Exception {
        return new DurableImpl(this, (DurableImpl) _parent, _unmodifiable);
    }
}
