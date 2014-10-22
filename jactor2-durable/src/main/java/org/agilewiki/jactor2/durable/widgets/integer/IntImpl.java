package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.*;

import java.nio.ByteBuffer;

public class IntImpl extends DurableImpl {

    public static DurableTransaction setValueTransaction(final String _path, final int _value) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(_value).rewind();
        return new DurableTransaction(_path, "setValue",
                new UnmodifiableByteBufferFactory(bb));
    }

    public static DurableTransaction expectTransaction(final String _path, final int _value) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(_value).rewind();
        return new DurableTransaction(_path, "expect",
                new UnmodifiableByteBufferFactory(bb));
    }

    protected Integer value = 0;

    public IntImpl(final IntFactory _widgetFactory,
                   final DurableImpl _parent,
                   final ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
        if (byteBuffer != null) {
            value = null;

        }
    }

    public IntImpl(final CFacility _facility,
                   final DurableImpl _parent,
                   final Integer _value) {
        super(IntFactory.getFactory(_facility), _parent, null);
        value = _value == null ? 0 : _value;
    }

    @Override
    public IntFactory getInternalWidgetFactory() {
        return (IntFactory) super.getInternalWidgetFactory();
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
            int newValue = _contentFactory.duplicateByteBuffer().getInt();
            asWidget().setValue(newValue);
            return "" + old + " -> " + value;
        }
        if ("expect".equals(_params)) {
            int old = value;
            int newValue = _contentFactory.duplicateByteBuffer().getInt();
            asWidget().expect(newValue);
            return null;
        }
        throw new InvalidDurableParamsException(_params);
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new IntImpl(getInternalWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    @Override
    public int getBufferSize() {
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
        public Integer getValue() {
            if (value == null)
                deserialize();
            return value;
        }

        @Override
        public void setValue(Integer _value) throws InvalidDurableContentException {
            if (_value == null)
                throw new InvalidDurableContentException("null is not valid");
            value = _value;
            byteBuffer = null;
        }

        @Override
        public void expect(Integer _value) throws UnexpectedValueException {
            if (value == null)
                deserialize();
            if (value != _value)
                throw new UnexpectedValueException("expected " + _value + ", not " + value);
        }
    }
}
