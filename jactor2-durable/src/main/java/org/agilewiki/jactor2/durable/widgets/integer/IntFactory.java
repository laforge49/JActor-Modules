package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.*;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

import java.nio.ByteBuffer;

public class IntFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "int";

    public static DurableInt newDurableInt(final CFacility _facility,
                                           final Widget _parent,
                                           final Integer _value) {
        IntFactory factory = getFactory(_facility);
        return factory.new IntImpl(_parent, _value).asWidget();
    }

    public static DurableInt newDurableInt(final CFacility _facility, final Integer _value) {
        IntFactory factory = getFactory(_facility);
        return factory.new IntImpl(null, _value).asWidget();
    }

    public static DurableTransaction setValueTransaction(final CFacility _facility,
                                                         final String _path, final int _value) {
        return new DurableTransaction(_path, "setValue",
                newDurableInt(_facility, _value));
    }

    public static DurableTransaction expectTransaction(final CFacility _facility,
                                                       final String _path, final int _value) {
        return new DurableTransaction(_path, "expect",
                newDurableInt(_facility, _value));
    }

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addWidgetFactorySOp(new IntFactory(_facility));
    }

    public static IntFactory getFactory(final CFacility _facility) {
        return (IntFactory) _facility.getWidgetFactory(FACTORY_NAME);
    }

    public static String factoryKey(final CFacility _facility) {
        return getFactory(_facility).getFactoryKey();
    }

    public IntFactory(CFacility _facility) {
        super(FACTORY_NAME, _facility);
    }

    public IntFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public DurableInt newWidget(final Widget _parent,
                                final ByteBuffer _byteBuffer) {
        return new IntImpl(_parent, _byteBuffer).asWidget();
    }

    protected class IntImpl extends WidgetImpl {

        protected Integer value = 0;

        public IntImpl(final Widget _parent,
                       final ByteBuffer _byteBuffer) {
            super(_parent, _byteBuffer);
            if (byteBuffer != null) {
                value = null;
            }
        }

        public IntImpl(final Widget _parent,
                       final Integer _value) {
            super(_parent, null);
            value = _value == null ? 0 : _value;
        }

        @Override
        protected _Int asWidget() {
            return (_Int) super.asWidget();
        }

        @Override
        protected _Int newWidget() {
            return new _Int();
        }

        @Override
        protected int getBufferSize() {
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
                Widget iw = getWidgetFactory().getFacility().newWidget(_contentType, null,
                        _contentFactory.duplicateByteBuffer());
                if (!(iw instanceof DurableInt))
                    throw new UnexpectedValueException("expected " +
                            IntFactory.factoryKey(getWidgetFactory().getFacility()) + " content type, not " +
                            iw.getWidgetFactory().getFactoryKey());
                DurableInt ii = (DurableInt) iw;
                if ("setValue".equals(_params)) {
                    int old = value;
                    asWidget().setValue(ii.getValue());
                    return "" + old + " -> " + value;
                }
                if ("expect".equals(_params)) {
                    int old = value;
                    asWidget().expect(ii.getValue());
                    return null;
                }
                throw new InvalidWidgetParamsException(_params);
            }

            @Override
            public _Widget recreate(UnmodifiableByteBufferFactory _unmodifiable) {
                return new IntImpl(getWidgetParent(), _unmodifiable.duplicateByteBuffer()).asWidget();
            }
        }
    }
}
