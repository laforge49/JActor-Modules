package org.agilewiki.jactor2.durable.widgets.string;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;

import java.nio.ByteBuffer;

public class StringFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "str";

    public static DurableString newDurableString(final CFacility _facility,
                                                 final Widget _parent,
                                                 final String _value) {
        return new StringImpl(getFactory(_facility), _parent, _value).asWidget();
    }

    public static DurableString newDurableString(final CFacility _facility, final String _value) {
        return new StringImpl(getFactory(_facility), null, _value).asWidget();
    }

    public static DurableTransaction setValueTransaction(final CFacility _facility,
                                                         final String _path, final String _value) {
        return new DurableTransaction(_path, "setValue", new StringImpl(getFactory(_facility), null, _value).asWidget());
    }

    public static DurableTransaction expectTransaction(final CFacility _facility,
                                                       final String _path, final String _value) {
        return new DurableTransaction(_path, "expect", new StringImpl(getFactory(_facility), null, _value).asWidget());
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
        return new StringImpl(this, _parent, _byteBuffer).asWidget();
    }
}
