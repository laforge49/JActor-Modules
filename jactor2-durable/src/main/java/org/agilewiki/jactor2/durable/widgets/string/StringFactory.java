package org.agilewiki.jactor2.durable.widgets.string;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.core.requests.SOp;

import java.nio.ByteBuffer;

public class StringFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "str";

    public static StringImpl._String newDurableString(final CFacility _facility, final String _value) {
        return new StringImpl(_facility, null, _value).asWidget();
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
    public StringImpl newInternalWidget(final InternalWidget _parent,
                                        final ByteBuffer _byteBuffer) {
        return new StringImpl(this, (StringImpl) _parent, _byteBuffer);
    }
}
