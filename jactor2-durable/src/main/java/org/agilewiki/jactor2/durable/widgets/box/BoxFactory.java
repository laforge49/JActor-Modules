package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.*;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetContentException;
import org.agilewiki.jactor2.durable.widgets.UnexpectedValueException;
import org.agilewiki.jactor2.durable.widgets.string.DurableString;
import org.agilewiki.jactor2.durable.widgets.string.StringFactory;

import java.nio.ByteBuffer;

public class BoxFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "box";

    public static DurableBox newDurableBox(final CFacility _facility, final Widget _parent) {
        BoxFactory factory = getFactory(_facility);
        return factory.new BoxImpl(_parent).asWidget();
    }

    public static DurableBox newDurableBox(final CFacility _facility) {
        BoxFactory factory = getFactory(_facility);
        return factory.new BoxImpl(null).asWidget();
    }

    public static DurableTransaction expectedFactoryKeyTransaction(final CFacility _facility,
                                                                   final String _path,
                                                                   final String _value) {
        return new DurableTransaction(_path, "expectedFactoryKey",
                StringFactory.newDurableString(_facility, null, _value));
    }

    public static DurableTransaction putCopyTransaction(final CFacility _facility,
                                                        final String _path,
                                                        final Widget _value) {
        return new DurableTransaction(_path, "putCopy", _value);
    }

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addWidgetFactorySOp(new BoxFactory(_facility));
    }

    public static BoxFactory getFactory(final CFacility _facility) {
        return (BoxFactory) _facility.getWidgetFactory(FACTORY_NAME);
    }

    public static String factoryKey(final CFacility _facility) {
        return getFactory(_facility).getFactoryKey();
    }

    public BoxFactory(CFacility _facility) {
        super(FACTORY_NAME, _facility);
    }

    public BoxFactory(String _name, CFacility _facility) {
        super(_name, _facility);
    }

    @Override
    public DurableBox newWidget(final Widget _parent,
                                final ByteBuffer _byteBuffer) {
        return new BoxImpl(_parent, _byteBuffer).asWidget();
    }

    protected class BoxImpl extends WidgetImpl {

        protected Widget content;

        protected DurableString factoryKey;

        protected int byteLen;

        public BoxImpl(Widget _parent, ByteBuffer _byteBuffer) {
            super(_parent, _byteBuffer);
            CFacility facility = getFacility();
            factoryKey = (DurableString) facility.newWidget(StringFactory.FACTORY_NAME, this.asWidget(),
                    _byteBuffer);
            content = facility.newWidget(factoryKey.getValue(), this.asWidget(),
                    _byteBuffer);
            byteLen = factoryKey.getBufferSize() + content.getBufferSize();
        }

        public BoxImpl(Widget _parent) {
            super(_parent, null);
            CFacility facility = getFacility();
            content = WidgetFactory.newWidget(facility, this.asWidget());
            factoryKey = StringFactory.newDurableString(facility, this.asWidget(),
                    content.getWidgetFactory().getFactoryKey());
            byteLen = factoryKey.getBufferSize() + content.getBufferSize();
        }

        @Override
        public _Box asWidget() {
            return (_Box) super.asWidget();
        }

        @Override
        protected _Box newWidget() {
            return new _Box();
        }

        @Override
        protected int getBufferSize() {
            return byteLen;
        }

        @Override
        protected void _serialize(final ByteBuffer _byteBuffer) {
            factoryKey.serialize(_byteBuffer);
            content.serialize(_byteBuffer);
        }

        protected class _Box extends _Widget implements DurableBox {
            @Override
            public Widget resolve(final String _path) {
                if (_path.length() == 0)
                    return this;
                if ("factoryKey".equals(_path))
                    return factoryKey;
                if ("content".equals(_path))
                    return content;
                if (_path.startsWith("content/"))
                    return content.resolve(_path.substring(8));
                return null;
            }

            @Override
            public String boxedFactoryKey() {
                return factoryKey.getValue();
            }

            @Override
            public void expectedFactoryKey(String _value) throws UnexpectedValueException {
                if (!_value.equals(factoryKey.getValue()))
                    throw new UnexpectedValueException("expected " + _value + ", not " + factoryKey.getValue());
            }

            @Override
            public Widget getContent() {
                return content;
            }

            @Override
            public void putCopy(Widget _widget) throws InvalidWidgetContentException {
                factoryKey.setValue(_widget.getWidgetFactory().getFactoryKey());
                int oldContentLength = content.getBufferSize();
                content.clearWidgetParent();
                content = new WidgetImpl(BoxImpl.this.asWidget(), _widget.createUnmodifiable().duplicateByteBuffer()).asWidget();
                byteBuffer = null;
                int delta = content.getBufferSize() - oldContentLength;
                byteLen += delta;
                notifyParent(delta);
            }

            @Override
            public String apply(final String _params, final String _contentType,
                                final UnmodifiableByteBufferFactory _contentFactory)
                    throws WidgetException {
                if ("expectedFactoryKey".equals(_params)) {
                    String newValue = StringFactory.readString(_contentFactory.duplicateByteBuffer());
                    expectedFactoryKey(newValue);
                    return null;
                }
                if ("putCopy".equals(_params)) {
                    WidgetFactory iwf =
                            getWidgetFactory().getFacility().getWidgetFactory(_contentType);
                    Widget iw = iwf.newWidget(BoxImpl.this.asWidget(),
                            _contentFactory.duplicateByteBuffer());
                    factoryKey.setValue(iwf.getFactoryKey());
                    int oldContentLength = content.getBufferSize();
                    content.clearWidgetParent();
                    content = iw;
                    byteBuffer = null;
                    int delta = content.getBufferSize() - oldContentLength;
                    byteLen += delta;
                    notifyParent(delta);
                    return null;
                }
                throw new InvalidWidgetParamsException(_params);
            }

            @Override
            public _Widget recreate(UnmodifiableByteBufferFactory _unmodifiable) {
                return new BoxImpl(getWidgetParent(), _unmodifiable.duplicateByteBuffer()).asWidget();
            }

            @Override
            public void childChange(final int _delta) {
                byteLen += _delta;
                notifyParent(_delta);
            }
        }
    }
}
