package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.durable.widgets.DurableFactory;
import org.agilewiki.jactor2.durable.widgets.DurableImpl;

import java.nio.ByteBuffer;

public class BoxImpl extends DurableImpl {
    public BoxImpl(DurableFactory _widgetFactory, InternalWidget _parent, ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
    }
}
