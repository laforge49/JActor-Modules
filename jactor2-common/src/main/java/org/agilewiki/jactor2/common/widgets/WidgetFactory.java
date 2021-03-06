package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.messages.SOp;

import java.nio.ByteBuffer;

public class WidgetFactory {

    public static final String FACTORY_NAME = "null";

    public static Widget newWidget(final CFacility _facility, final Widget _parent) {
        WidgetFactory factory = getFactory(_facility);
        return factory.new WidgetImpl(_parent).asWidget();
    }

    public static Widget newWidget(final CFacility _facility) {
        WidgetFactory factory = getFactory(_facility);
        return factory.new WidgetImpl(null).asWidget();
    }

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addWidgetFactorySOp(new WidgetFactory(_facility));
    }

    public static WidgetFactory getFactory(final CFacility _facility) {
        return _facility.getWidgetFactory(FACTORY_NAME);
    }

    public static String factoryKey(final CFacility _facility) {
        return getFactory(_facility).getFactoryKey();
    }

    private final String name;
    private final CFacility facility;

    public WidgetFactory(CFacility _facility) {
        this(FACTORY_NAME, _facility);
    }

    public WidgetFactory(final String _name, final CFacility _facility) {
        if (_name.contains("."))
            throw new IllegalArgumentException("name must not contain a .");
        name = _name;
        facility = _facility;
    }

    public String getName() {
        return name;
    }

    public CFacility getFacility() {
        return facility;
    }

    public String getFactoryKey() {
        return getFacility().name + "." + name;
    }

    public Widget newWidget(final Widget _parent,
                            ByteBuffer _byteBuffer) {
        return new WidgetImpl(_parent, _byteBuffer).asWidget();
    }

    protected class WidgetImpl {
        private final Widget widget;
        private Widget widgetParent;
        protected ByteBuffer byteBuffer;

        protected WidgetImpl(final Widget _parent,
                             final ByteBuffer _byteBuffer) {
            widgetParent = _parent;
            initBuffer(_byteBuffer);
            widget = newWidget();
            if (byteBuffer != null)
                byteBuffer.limit(getBufferSize());
        }

        protected WidgetImpl(final Widget _parent) {
            this(_parent, null);
        }

        protected void initBuffer(final ByteBuffer _byteBuffer) {
            if (_byteBuffer == null)
                return;
            int startPosition = _byteBuffer.position();
            byteBuffer = _byteBuffer.asReadOnlyBuffer().slice();
            readLength(_byteBuffer);
            _byteBuffer.position(startPosition + getBufferSize());
        }

        protected void readLength(final ByteBuffer _bb) {
        }

        protected int getBufferSize() {
            return 0;
        }

        protected void notifyParent(int _delta) {
            if (widgetParent != null)
                widgetParent.childChange(_delta);
        }

        protected void _serialize(final ByteBuffer _byteBuffer) {
        }

        protected void deserialize() {
            _deserialize();
            byteBuffer.rewind();
        }

        protected void _deserialize() {
        }

        protected Widget asWidget() {
            return widget;
        }

        protected Widget newWidget() {
            Widget w = new _Widget();
            return w;
        }

        protected class _Widget implements Widget {
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
                return widgetParent;
            }

            @Override
            public WidgetFactory getWidgetFactory() {
                return WidgetFactory.this;
            }

            @Override
            public Widget deepCopy() {
                return new WidgetImpl(null, createUnmodifiable().duplicateByteBuffer()).asWidget();
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
            public Widget recreate(UnmodifiableByteBufferFactory _unmodifiable) {
                return new WidgetImpl(getWidgetParent(), _unmodifiable.duplicateByteBuffer()).asWidget();
            }

            @Override
            public int getBufferSize() {
                return WidgetImpl.this.getBufferSize();
            }

            @Override
            public void clearWidgetParent() {
                widgetParent = null;
            }

            @Override
            public void serialize(final ByteBuffer _byteBuffer) {
                byte[] bytes = null;
                if (byteBuffer != null) {
                    int rem = byteBuffer.limit() - byteBuffer.position();
                    bytes = new byte[getBufferSize()];
                    byteBuffer.get(bytes);
                }
                byteBuffer = _byteBuffer.asReadOnlyBuffer().slice();
                byteBuffer.limit(getBufferSize());
                if (bytes == null) {
                    _serialize(_byteBuffer);
                } else {
                    _byteBuffer.put(bytes);
                }
            }

            @Override
            public void childChange(final int _delta) {
                throw new UnsupportedOperationException("not a container");
            }
        }
    }
}
