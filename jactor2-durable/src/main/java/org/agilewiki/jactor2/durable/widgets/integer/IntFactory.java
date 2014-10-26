package org.agilewiki.jactor2.durable.widgets.integer;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;

import java.nio.ByteBuffer;

public class IntFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "int";

    public static DurableInt newDurableInt(final CFacility _facility,
                                           final Widget _parent,
                                           final Integer _value) {
        return new IntImpl(getFactory(_facility), _parent, _value).asWidget();
    }

    public static DurableInt newDurableInt(final CFacility _facility, final Integer _value) {
        return new IntImpl(getFactory(_facility), null, _value).asWidget();
    }

    public static DurableTransaction setValueTransaction(final CFacility _facility,
                                                         final String _path, final int _value) {
        return new DurableTransaction(_path, "setValue", new IntImpl(getFactory(_facility), null, _value).asWidget());
    }

    public static DurableTransaction expectTransaction(final CFacility _facility,
                                                       final String _path, final int _value) {
        return new DurableTransaction(_path, "expect", new IntImpl(getFactory(_facility), null, _value).asWidget());
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
        return new IntImpl(this, _parent, _byteBuffer).asWidget();
    }
}
