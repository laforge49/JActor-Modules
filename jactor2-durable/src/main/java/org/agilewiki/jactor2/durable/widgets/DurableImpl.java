package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

/**
 * Implements Durable as a nested class.
 */
public class DurableImpl extends WidgetImpl {

    public DurableImpl(final DurableFactory _widgetFactory,
                       final DurableImpl _parent,
                       final ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
    }

    @Override
    public DurableFactory getWidgetFactory() {
        return (DurableFactory) super.getWidgetFactory();
    }

    @Override
    public DurableImpl getParent() {
        return (DurableImpl) super.getParent();
    }

    @Override
    public _Durable asWidget() {
        return (_Durable) super.asWidget();
    }

    @Override
    protected _Durable newWidget() {
        return new _Durable();
    }

    public UnmodifiableByteBufferFactory apply(final String _path,
                                               final String _params,
                                               final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception {
        throw new InvalidDurableParamsException(_params);
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new DurableImpl(getWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    protected class _Durable extends _Widget implements Durable {
    }
}
