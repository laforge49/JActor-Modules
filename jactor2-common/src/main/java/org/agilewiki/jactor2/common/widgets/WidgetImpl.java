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
            return;
        startPosition = _byteBuffer.position();
        int endPosition = startPosition + getLength();
        byteBuffer = _byteBuffer.asReadOnlyBuffer();
        _byteBuffer.position(endPosition);
        byteBuffer.limit(endPosition);
    }

    public int getLength() {
        return 0;
    }

    protected void serialize(final ByteBuffer _byteBuffer) {
        startPosition = _byteBuffer.position();
        _serialize(_byteBuffer);
        int endPosition = startPosition + getLength();
        byteBuffer = _byteBuffer.asReadOnlyBuffer();
        byteBuffer.position(startPosition);
        byteBuffer.limit(endPosition);
    }

    protected void _serialize(final ByteBuffer _byteBuffer) {
    }

    protected void deserializde() {
        _deserialize();
        byteBuffer.position(startPosition);
    }

    protected void _deserialize() {
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
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(getLength());
            serialize(byteBuffer);
            byteBuffer.rewind();
        }
        return new UnmodifiableByteBufferFactory(byteBuffer);
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(final UnmodifiableByteBufferFactory _unmodifiable) {
        return new WidgetImpl(getWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    protected class _Widget implements Widget {
    }
}
