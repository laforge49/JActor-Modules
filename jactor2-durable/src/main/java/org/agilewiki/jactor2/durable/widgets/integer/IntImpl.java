package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InvalidWidgetParamsException;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetException;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;

import java.nio.ByteBuffer;

public class IntImpl extends WidgetImpl {

    protected Integer value = 0;

    public IntImpl(final IntFactory _widgetFactory,
                   final Widget _parent,
                   final ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
        if (byteBuffer != null) {
            value = null;
        }
    }

    public IntImpl(final IntFactory _widgetFactory,
                   final Widget _parent,
                   final Integer _value) {
        super(_widgetFactory, _parent, null);
        value = _value == null ? 0 : _value;
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
    public IntImpl recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new IntImpl(getWidgetFactory(),
                getWidgetParent(), _unmodifiable.duplicateByteBuffer());
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
    }
}
