package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

/**
 * Implements Widget as a nested class.
 */
public class WidgetImpl implements InternalWidget {
    private final InternalWidgetFactory widgetFactory;
    private final _Widget widget;
    private InternalWidget internalWidgetParent;
    protected ByteBuffer byteBuffer;

    public WidgetImpl(final InternalWidgetFactory _widgetFactory,
                      final InternalWidget _parent,
                      final ByteBuffer _byteBuffer) {
        widgetFactory = _widgetFactory;
        internalWidgetParent = _parent;
        initBuffer(_byteBuffer);
        widget = newWidget();
    }

    public WidgetImpl(final CFacility _facility,
                      final InternalWidget _parent) {
        this(InternalWidgetFactory.getFactory(_facility), _parent, null);
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
        if (internalWidgetParent != null)
            internalWidgetParent.childChange(_delta);
    }

    public void clearInternalWidgetParent() {
        internalWidgetParent = null;
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
    public InternalWidgetFactory getInternalWidgetFactory() {
        return widgetFactory;
    }

    /**
     * Returns the container widget.
     *
     * @return The container widget, or null.
     */
    public InternalWidget getInternalWidgetParent() {
        return internalWidgetParent;
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
    public WidgetImpl recreate(final UnmodifiableByteBufferFactory _unmodifiable) {
        return new WidgetImpl(getInternalWidgetFactory(),
                getInternalWidgetParent(), _unmodifiable.duplicateByteBuffer());
    }

    public WidgetImpl deepCopy(final InternalWidget _parent) {
        return new WidgetImpl(getInternalWidgetFactory(),
                _parent, createUnmodifiable().duplicateByteBuffer());
    }

    public class _Widget implements Widget {
        @Override
        public _Widget resolve(final String _path) {
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
        public Widget getWidget() {
            return getInternalWidgetParent().asWidget();
        }

        @Override
        public InternalWidgetFactory getInternalWidgetFactory() {
            return widgetFactory;
        }

        @Override
        public UnmodifiableByteBufferFactory createUnmodifiable() {
            return WidgetImpl.this.createUnmodifiable();
        }

        @Override
        public _Widget recreate(UnmodifiableByteBufferFactory _unmodifiable) {
            return WidgetImpl.this.recreate(_unmodifiable).asWidget();
        }
    }
}
