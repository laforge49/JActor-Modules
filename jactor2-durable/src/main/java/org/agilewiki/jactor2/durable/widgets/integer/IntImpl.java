package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.InvalidWidgetParamsException;
import org.agilewiki.jactor2.common.widgets.WidgetException;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

import java.nio.ByteBuffer;

public class IntImpl extends WidgetImpl {

    public static DurableTransaction setValueTransaction(final CFacility facility,
                                                         final String _path, final int _value) {
        return new DurableTransaction(_path, "setValue", new IntImpl(facility, null, _value));
    }

    public static DurableTransaction expectTransaction(final CFacility facility,
                                                       final String _path, final int _value) {
        return new DurableTransaction(_path, "expect", new IntImpl(facility, null, _value));
    }

    protected Integer value = 0;

    public IntImpl(final IntFactory _widgetFactory,
                   final InternalWidget _parent,
                   final ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
        if (byteBuffer != null) {
            value = null;

        }
    }

    public IntImpl(final CFacility _facility,
                   final InternalWidget _parent,
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
    public IntImpl recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new IntImpl(getInternalWidgetFactory(),
                getInternalWidgetParent(), _unmodifiable.duplicateByteBuffer());
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

    public class _Int extends _Widget implements DurableInt {
        @Override
        public Integer getValue() {
            if (value == null)
                deserialize();
            return value;
        }

        @Override
        public void setValue(Integer _value) throws InvalidWidgetContentException {
            if (_value == null)
                throw new InvalidWidgetContentException("null is not valid");
            value = _value;
            byteBuffer = null;
            notifyParent(0);
        }

        @Override
        public void expect(Integer _value) throws UnexpectedValueException {
            if (value == null)
                deserialize();
            if (value != _value)
                throw new UnexpectedValueException("expected " + _value + ", not " + value);
        }

        @Override
        public String apply(final String _params, final String _contentType,
                            final UnmodifiableByteBufferFactory _contentFactory)
                throws WidgetException {
            InternalWidget iw = getWidgetFactory().getFacility().newInternalWidget(_contentType, null,
                    _contentFactory.duplicateByteBuffer());
            if (!(iw instanceof IntImpl))
                throw new UnexpectedValueException("expected " +
                        IntFactory.factoryKey(getWidgetFactory().getFacility()) + " content type, not " +
                        iw.getInternalWidgetFactory().getFactoryKey());
            IntImpl ii = (IntImpl) iw;
            if ("setValue".equals(_params)) {
                int old = value;
                asWidget().setValue(ii.asWidget().getValue());
                return "" + old + " -> " + value;
            }
            if ("expect".equals(_params)) {
                int old = value;
                asWidget().expect(ii.asWidget().getValue());
                return null;
            }
            throw new InvalidWidgetParamsException(_params);
        }
    }
}
