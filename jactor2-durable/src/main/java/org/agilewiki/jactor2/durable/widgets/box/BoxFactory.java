package org.agilewiki.jactor2.durable.widgets.box;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.InternalWidgetFactory;
import org.agilewiki.jactor2.core.requests.SOp;

import java.nio.ByteBuffer;

public class BoxFactory extends InternalWidgetFactory {

    public static final String FACTORY_NAME = "box";

    public static SOp<Void> addFactorySOp(final CFacility _facility) {
        return _facility.addInternalWidgetFactorySOp(new BoxFactory(_facility));
    }

    public static BoxFactory getFactory(final CFacility _facility) {
        return (BoxFactory) _facility.getInternalWidgetFactory(FACTORY_NAME);
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
    public BoxImpl newInternalWidget(final InternalWidget _parent,
                                     final ByteBuffer _byteBuffer) {
        return new BoxImpl(this, (BoxImpl) _parent, _byteBuffer);
    }
}
