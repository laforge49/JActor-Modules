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
    private InternalWidget parent;
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
    public void childChange(int _delta) {
        throw new UnsupportedOperationException("not a container");
    }

    protected void notifyParent(int _delta) {
        if (parent != null)
            parent.childChange(_delta);
    }

    public void clearParent() {
        parent = null;
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

    /**
     * Returns the container widget.
     *
     * @return The container widget, or null.
     */
    public InternalWidget getParent() {
        return parent;
    }

    @Override
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

    public WidgetImpl deepCopy() {
        return (WidgetImpl) recreate(createUnmodifiable());
    }

    public class _Widget implements Widget {
        @Override
        public _Widget resolve(final String _path) {
            if (_path.length() == 0)
                return this;
            return null;
        }

        @Override
        public String apply(final String _params,
                            final UnmodifiableByteBufferFactory _contentFactory)
                throws WidgetException {
            throw new InvalidWidgetParamsException(_params);
        }
    }
}
