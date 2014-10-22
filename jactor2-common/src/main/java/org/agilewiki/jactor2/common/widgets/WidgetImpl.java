package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

/**
 * Implements Widget as a nested class.
 */
public class WidgetImpl implements InternalWidget {
    private final WidgetFactory widgetFactory;
    private final _Widget widget;
    private final InternalWidget parent;
    protected ByteBuffer byteBuffer;

    public WidgetImpl(final WidgetFactory _widgetFactory,
                      final InternalWidget _parent,
                      final ByteBuffer _byteBuffer) {
        widgetFactory = _widgetFactory;
        parent = _parent;
        initBuffer(_byteBuffer);
        widget = newWidget();
    }

    protected void initBuffer(final ByteBuffer _byteBuffer) {
        if (_byteBuffer == null)
            return;
        int startPosition = _byteBuffer.position();
        byteBuffer = _byteBuffer.asReadOnlyBuffer().slice();
        readLength(_byteBuffer);
        _byteBuffer.position(startPosition + getBufferSize());
        byteBuffer.limit(getBufferSize());
    }

    protected void readLength(final ByteBuffer _bb) {
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void serialize(final ByteBuffer _byteBuffer) {
        byte[] bytes = null;
        if (byteBuffer != null) {
            bytes = new byte[getBufferSize()];
            byteBuffer.get(bytes);
        }
        byteBuffer = _byteBuffer.asReadOnlyBuffer().slice();
        byteBuffer.limit(getBufferSize());
        if (bytes == null)
            _serialize(_byteBuffer);
        else
            _byteBuffer.put(bytes);
    }

    protected void _serialize(final ByteBuffer _byteBuffer) {
    }

    protected void deserialize() {
        _deserialize();
        byteBuffer.rewind();
    }

    protected void _deserialize() {
    }

    @Override
    public WidgetFactory getInternalWidgetFactory() {
        return widgetFactory;
    }

    @Override
    public InternalWidget getParent() {
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
            ByteBuffer _byteBuffer = ByteBuffer.allocate(getBufferSize());
            serialize(_byteBuffer);
            _byteBuffer.rewind();
            return new UnmodifiableByteBufferFactory(_byteBuffer);
        }
        return new UnmodifiableByteBufferFactory(byteBuffer);
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(final UnmodifiableByteBufferFactory _unmodifiable) {
        return new WidgetImpl(getInternalWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    protected class _Widget implements Widget {
    }
}
