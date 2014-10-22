package org.agilewiki.jactor2.durable.widgets.string;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.InvalidWidgetPathException;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.*;

import java.nio.ByteBuffer;

public class StringImpl extends DurableImpl {

    public static String readString(final ByteBuffer _bb) {
        int len = _bb.getInt();
        char[] chars = new char[len / 2];
        _bb.asCharBuffer().get(chars);
        _bb.position(_bb.position() + len);
        return new String(chars);
    }

    public static void writeString(final ByteBuffer _bb, final String _str) {
        int len = _str.length() * 2;
        _bb.putInt(len);
        _bb.asCharBuffer().put(_str);
        _bb.position(_bb.position() + len);
    }

    public static DurableTransaction setValueTransaction(final String _path, final String _value) {
        ByteBuffer bb = ByteBuffer.allocate(4 + 2 * _value.length());
        writeString(bb, _value);
        bb.rewind();
        return new DurableTransaction(_path, "setValue",
                new UnmodifiableByteBufferFactory(bb));
    }

    public static DurableTransaction expectTransaction(final String _path, final String _value) {
        ByteBuffer bb = ByteBuffer.allocate(4 + 2 * _value.length());
        writeString(bb, _value);
        bb.rewind();
        return new DurableTransaction(_path, "expect",
                new UnmodifiableByteBufferFactory(bb));
    }

    protected String value = "";

    protected int byteLen;

    public StringImpl(DurableFactory _widgetFactory, InternalWidget _parent, ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
        if (byteBuffer != null) {
            value = null;
        } else
            byteLen = 4 + 2 * value.length();
    }

    public StringImpl(final CFacility _facility,
                      final InternalWidget _parent,
                      final String _value) {
        super(StringFactory.getFactory(_facility), _parent, null);
        value = _value == null ? "" : _value;
        byteLen = 4 + 2 * value.length();
    }

    @Override
    protected void readLength(final ByteBuffer _bb) {
        byteLen = _bb.getInt() + 4;
    }

    @Override
    public StringFactory getInternalWidgetFactory() {
        return (StringFactory) super.getInternalWidgetFactory();
    }

    @Override
    public _String asWidget() {
        return (_String) super.asWidget();
    }

    @Override
    protected _String newWidget() {
        return new _String();
    }

    @Override
    public String apply(final String _path,
                        final String _params,
                        final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception {
        if (_path.length() != 0)
            throw new InvalidWidgetPathException(_path);
        if ("setValue".equals(_params)) {
            String old = value;
            String newValue = readString(_contentFactory.duplicateByteBuffer());
            asWidget().setValue(newValue);
            return "" + old + " -> " + value;
        }
        if ("expect".equals(_params)) {
            String old = value;
            String newValue = readString(_contentFactory.duplicateByteBuffer());
            asWidget().expect(newValue);
            return null;
        }
        throw new InvalidDurableParamsException(_params);
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new StringImpl(getInternalWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    @Override
    public int getBufferSize() {
        return byteLen;
    }

    @Override
    protected void _serialize(final ByteBuffer _byteBuffer) {
        writeString(_byteBuffer, value);
    }

    @Override
    protected void _deserialize() {
        value = readString(byteBuffer);
    }

    public class _String extends _Durable implements DurableString {
        @Override
        public String getValue() {
            if (value == null)
                deserialize();
            return value;
        }

        @Override
        public int length() {
            return (byteLen - 4) / 2;
        }

        @Override
        public void setValue(String _value) throws InvalidDurableContentException {
            if (_value == null)
                throw new InvalidDurableContentException("null is not valid");
            value = _value;
            int oldLen = byteLen;
            byteLen = 4 + 2 * value.length();
            byteBuffer = null;
            notifyParent(byteLen - oldLen);
        }

        @Override
        public void expect(String _value) throws UnexpectedValueException {
            if (value == null)
                deserialize();
            if (!value.equals(_value))
                throw new UnexpectedValueException("expected " + _value + ", not " + value);
        }
    }
}