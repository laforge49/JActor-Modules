package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

/**
 * Implements Widget as a nested class.
 */
public class WidgetImpl implements Transmutable<UnmodifiableByteBufferFactory> {
    private final WidgetFactory widgetFactory;
    private final _Widget widget;
    private final WidgetImpl parent;
    protected ByteBuffer byteBuffer;
    protected int startPosition;

    public WidgetImpl(final WidgetFactory _widgetFactory,
                      final WidgetImpl _parent,
                      final ByteBuffer _byteBuffer) {
        widgetFactory = _widgetFactory;
        parent = _parent;
        initBuffer(_byteBuffer);
        widget = newWidget();
    }

    protected void initBuffer(final ByteBuffer _byteBuffer) {
        if (_byteBuffer == null)
            byteBuffer = null;
        else {
            startPosition = _byteBuffer.position();
            int endPosition = startPosition + getLength();
            byteBuffer = _byteBuffer.asReadOnlyBuffer();
            _byteBuffer.position(endPosition);
            byteBuffer.limit(endPosition);
        }
    }

    protected int getLength() {
        return 0;
    }

    public WidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    public WidgetImpl getParent() {
        return parent;
    }

    public _Widget asWidget() {
        return widget;
    }

    protected _Widget newWidget() {
        return new _Widget();
    }

    @Override
    public UnmodifiableByteBufferFactory createUnmodifiable() {
        return new UnmodifiableByteBufferFactory(byteBuffer.array());
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(final UnmodifiableByteBufferFactory _unmodifiable) {
        return new WidgetImpl(getWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    protected class _Widget implements Widget {
    }
}
