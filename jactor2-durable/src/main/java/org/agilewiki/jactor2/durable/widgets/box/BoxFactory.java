package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.core.requests.SOp;
import org.agilewiki.jactor2.durable.transactions.DurableTransaction;
import org.agilewiki.jactor2.durable.widgets.string.StringFactory;

import java.nio.ByteBuffer;

public class BoxFactory extends WidgetFactory {

    public static final String FACTORY_NAME = "box";

    public static DurableBox newDurableBox(final CFacility _facility, final Widget _parent) {
        return new BoxImpl(_facility, _parent).asWidget();
    }

    public static DurableBox newDurableBox(final CFacility _facility) {
        return new BoxImpl(_facility, null).asWidget();
    }

    public static DurableTransaction expectedFactoryKeyTransaction(final CFacility facility,
                                                                   final String _path,
                                                                   final String _value) {
        return new DurableTransaction(_path, "expectedFactoryKey",
                StringFactory.newDurableString(facility, _value));
    }

    public static DurableTransaction putCopyTransaction(final CFacility facility,
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
        return new BoxImpl(this, _parent, _byteBuffer).asWidget();
    }
}
