package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;
import org.agilewiki.jactor2.durable.widgets.DurableImpl;
import org.agilewiki.jactor2.durable.widgets.InvalidDurableParamsException;
import org.agilewiki.jactor2.durable.widgets.InvalidDurablePathException;

import java.nio.ByteBuffer;

public class IntImpl extends DurableImpl {
    protected Integer value = 0;

    public IntImpl(final IntFactory _widgetFactory,
                   final DurableImpl _parent,
                   final ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
        if (byteBuffer != null)
            value = null;
    }

    @Override
    public IntFactory getWidgetFactory() {
        return (IntFactory) super.getWidgetFactory();
    }

    @Override
    public _Int asWidget() {
        return (_Int) super.asWidget();
    }

    @Override
    protected _Int newWidget() {
        return new _Int();
    }

    @Override
    public String apply(final String _path,
                                               final String _params,
                                               final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception {
        if (_path.length() != 0)
            throw new InvalidDurablePathException(_path);
        if ("setValue".equals(_params)) {
            int old = value;
            value = _contentFactory.duplicateByteBuffer().getInt();
            return "" + old + " -> " + value;
        }
        throw new InvalidDurableParamsException(_params);
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new IntImpl(getWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    @Override
    public int getLength() {
        return 4;
    }

    @Override
    protected void _serialize(final ByteBuffer _byteBuffer) {
        _byteBuffer.putInt(value);
    }

    @Override
    protected void _deserialize() {
        value = byteBuffer.getInt();
    }

    protected class _Int extends _Durable implements DurableInt {
        @Override
        public Integer getValue() throws Exception {
            if (value == null)
                deserializde();
            return value;
        }

        @Override
        public void setValue(Integer _value) {
            value = _value;
            byteBuffer = null;
        }
    }
}
