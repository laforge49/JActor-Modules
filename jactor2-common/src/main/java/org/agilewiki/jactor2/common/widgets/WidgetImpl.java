package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;

import java.nio.ByteBuffer;

/**
 * Implements Widget as a nested class.
 */
public class WidgetImpl {
    private final WidgetFactory widgetFactory;
    private final _Widget widget;
    private Widget internalWidgetParent;
    protected ByteBuffer byteBuffer;

    public WidgetImpl(final WidgetFactory _widgetFactory,
                      final Widget _parent,
                      final ByteBuffer _byteBuffer) {
        widgetFactory = _widgetFactory;
        internalWidgetParent = _parent;
        initBuffer(_byteBuffer);
        widget = newWidget();
    }

    public WidgetImpl(final CFacility _facility,
                      final Widget _parent) {
        this(WidgetFactory.getFactory(_facility), _parent, null);
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

    public int getBufferSize() {
        return 0;
    }

    public void childChange(int _delta) {
        throw new UnsupportedOperationException("not a container");
    }

    protected void notifyParent(int _delta) {
        if (internalWidgetParent != null)
            internalWidgetParent.childChange(_delta);
    }

    public void clearWidgetParent() {
        internalWidgetParent = null;
    }

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

    public WidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    /**
     * Returns the container widget.
     *
     * @return The container widget, or null.
     */
    public Widget getInternalWidgetParent() {
        return internalWidgetParent;
    }

    public _Widget asWidget() {
        return widget;
    }

    protected _Widget newWidget() {
        return new _Widget();
    }

    public UnmodifiableByteBufferFactory createUnmodifiable() {
        if (byteBuffer == null) {
            ByteBuffer _byteBuffer = ByteBuffer.allocate(getBufferSize());
            serialize(_byteBuffer);
            _byteBuffer.rewind();
            return new UnmodifiableByteBufferFactory(_byteBuffer);
        }
        return new UnmodifiableByteBufferFactory(byteBuffer);
    }

    public WidgetImpl recreate(final UnmodifiableByteBufferFactory _unmodifiable) {
        return new WidgetImpl(getWidgetFactory(),
                getInternalWidgetParent(), _unmodifiable.duplicateByteBuffer());
    }

    public Widget deepCopy(final Widget _parent) {
        return new WidgetImpl(getWidgetFactory(),
                _parent, createUnmodifiable().duplicateByteBuffer()).asWidget();
    }

    public class _Widget implements Widget {
        @Override
        public Widget resolve(final String _path) {
            if (_path.length() == 0)
                return this;
            return null;
        }

        @Override
        public String apply(final String _params, final String _contentType,
                            final UnmodifiableByteBufferFactory _contentFactory)
                throws WidgetException {
            throw new InvalidWidgetParamsException(_params);
        }

        @Override
        public Widget getWidgetParent() {
            return getInternalWidgetParent();
        }

        @Override
        public WidgetFactory getWidgetFactory() {
            return widgetFactory;
        }

        @Override
        public Widget deepCopy() {
            return WidgetImpl.this.deepCopy(null);
        }

        @Override
        public UnmodifiableByteBufferFactory createUnmodifiable() {
            return WidgetImpl.this.createUnmodifiable();
        }

        @Override
        public _Widget recreate(UnmodifiableByteBufferFactory _unmodifiable) {
            return WidgetImpl.this.recreate(_unmodifiable).asWidget();
        }

        @Override
        public int getBufferSize() {
            return WidgetImpl.this.getBufferSize();
        }

        @Override
        public void clearWidgetParent() {
            WidgetImpl.this.clearWidgetParent();
        }

        @Override
        public void serialize(final ByteBuffer _byteBuffer) {
            WidgetImpl.this.serialize(_byteBuffer);
        }

        @Override
        public void childChange(final int _delta) {
            WidgetImpl.this.childChange(_delta);
        }
    }
}
