package org.agilewiki.jactor2.durable.widgets.string;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InvalidWidgetParamsException;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetException;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

import java.nio.ByteBuffer;

public class StringFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "str";

    public static DurableString newDurableString(final CFacility _facility,
                                                 final Widget _parent,
                                                 final String _value) {
        StringFactory factory = getFactory(_facility);
        return factory.new StringImpl(_parent, _value).asWidget();
    }

    public static DurableString newDurableString(final CFacility _facility, final String _value) {
        StringFactory factory = getFactory(_facility);
        return factory.new StringImpl(null, _value).asWidget();
    }

    public static DurableTransaction setValueTransaction(final CFacility _facility,
                                                         final String _path, final String _value) {
        return new DurableTransaction(_path, "setValue", newDurableString(_facility, null, _value));
    }

    public static DurableTransaction expectTransaction(final CFacility _facility,
                                                       final String _path, final String _value) {
        return new DurableTransaction(_path, "expect", newDurableString(_facility, null, _value));
    }

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

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addWidgetFactorySOp(new StringFactory(_facility));
    }

    public static StringFactory getFactory(final CFacility _facility) {
        return (StringFactory) _facility.getWidgetFactory(FACTORY_NAME);
    }

    public static String factoryKey(final CFacility _facility) {
        return getFactory(_facility).getFactoryKey();
    }

    public StringFactory(CFacility _facility) {
        super(FACTORY_NAME, _facility);
    }

    public StringFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public DurableString newWidget(final Widget _parent,
                                   final ByteBuffer _byteBuffer) {
        return new StringImpl(_parent, _byteBuffer).asWidget();
    }

    protected class StringImpl extends WidgetImpl {

        protected String value = "";

        protected int byteLen;

        protected StringImpl(final Widget _parent,
                             final ByteBuffer _byteBuffer) {
            super(_parent, _byteBuffer);
            if (byteBuffer != null) {
                value = null;
                byteBuffer.limit(getBufferSize());
            } else
                byteLen = 4 + 2 * value.length();
        }

        protected StringImpl(final Widget _parent,
                             final String _value) {
            super(_parent, null);
            value = _value == null ? "" : _value;
            byteLen = 4 + 2 * value.length();
        }

        @Override
        protected void readLength(final ByteBuffer _bb) {
            byteLen = _bb.getInt() + 4;
        }

        @Override
        protected DurableString asWidget() {
            return (_String) super.asWidget();
        }

        @Override
        protected DurableString newWidget() {
            return new _String();
        }

        @Override
        protected int getBufferSize() {
            return byteLen;
        }

        @Override
        protected void _serialize(final ByteBuffer _byteBuffer) {
            StringFactory.writeString(_byteBuffer, value);
        }

        @Override
        protected void _deserialize() {
            value = StringFactory.readString(byteBuffer);
        }

        protected class _String extends _Widget implements DurableString {
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
            public void setValue(String _value) throws InvalidWidgetContentException {
                if (_value == null)
                    throw new InvalidWidgetContentException("null is not valid");
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

            @Override
            public String apply(final String _params, final String _contentType,
                                final UnmodifiableByteBufferFactory _contentFactory)
                    throws WidgetException {
                Widget iw = getWidgetFactory().getFacility().newWidget(_contentType, null,
                        _contentFactory.duplicateByteBuffer());
                if (!(iw instanceof DurableString))
                    throw new UnexpectedValueException(
                            "expected " + StringFactory.factoryKey(getWidgetFactory().getFacility()) +
                                    " content type, not " +
                                    iw.getWidgetFactory().getFactoryKey());
                DurableString ii = (DurableString) iw;
                if ("setValue".equals(_params)) {
                    String old = value;
                    asWidget().setValue(ii.getValue());
                    return "" + old + " -> " + value;
                }
                if ("expect".equals(_params)) {
                    asWidget().expect(ii.getValue());
                    return null;
                }
                throw new InvalidWidgetParamsException(_params);
            }

            @Override
            public DurableString recreate(UnmodifiableByteBufferFactory _unmodifiable) {
                return new StringImpl(getWidgetParent(), _unmodifiable.duplicateByteBuffer()).asWidget();
            }
        }
    }
}
