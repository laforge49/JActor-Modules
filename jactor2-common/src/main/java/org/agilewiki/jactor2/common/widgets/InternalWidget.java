package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

public interface InternalWidget extends Transmutable<UnmodifiableByteBufferFactory> {
    int getLength();
    void serialize(final ByteBuffer _byteBuffer);
    InternalWidgetFactory getInternalWidgetFactory();
    InternalWidget getParent();
}
