package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;

import java.nio.ByteBuffer;

public abstract class WidgetFactory extends InternalWidgetFactory {
    public WidgetFactory(final String _name, final CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public abstract WidgetImpl newInternalWidget(final InternalWidget _parent,
                                                 ByteBuffer _byteBuffer);
}
