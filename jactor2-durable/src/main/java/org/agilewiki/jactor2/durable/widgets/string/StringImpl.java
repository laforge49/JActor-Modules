package org.agilewiki.jactor2.durable.widgets.string;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.*;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

import java.nio.ByteBuffer;

public class StringImpl extends WidgetImpl {

    protected String value = "";

    protected int byteLen;

    public StringImpl(final WidgetFactory _widgetFactory,
                      final Widget _parent,
                      final ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
        if (byteBuffer != null) {
            value = null;
        } else
            byteLen = 4 + 2 * value.length();
    }

    public StringImpl(final CFacility _facility,
                      final Widget _parent,
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
    public StringFactory getWidgetFactory() {
        return (StringFactory) super.getWidgetFactory();
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
    public StringImpl recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new StringImpl(getWidgetFactory(),
                getInternalWidgetParent(), _unmodifiable.duplicateByteBuffer());
    }

    @Override
    public int getBufferSize() {
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

    public class _String extends _Widget implements DurableString {
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
            Widget iw = getWidgetFactory().getFacility().newInternalWidget(_contentType, null,
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
    }
}
