package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

/**
 * Implements Durable as a nested class.
 */
public class DurableImpl extends WidgetImpl<UnmodifiableByteBufferFactory> {
    public DurableImpl(final DurableFactory _widgetFactory, final DurableImpl _parent) {
        super(_widgetFactory, _parent);
    }

    public DurableImpl(final DurableFactory _widgetFactory,
                       final DurableImpl _parent,
                       final UnmodifiableByteBufferFactory _unmodifiableByteBuffer) {
        super(_widgetFactory, _parent);
        asWidget().unmodifiableByteBufferFactory = _unmodifiableByteBuffer;
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
        return  (_Durable) super.asWidget();
    }

    @Override
    protected _Durable newWidget() {
        return new _Durable();
    }

    protected class _Durable extends _Widget<UnmodifiableByteBufferFactory> implements Durable {
        UnmodifiableByteBufferFactory unmodifiableByteBufferFactory;

        @Override
        public UnmodifiableByteBufferFactory apply(String _path,
                                                   String _params,
                                                   UnmodifiableByteBufferFactory _contentFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public UnmodifiableByteBufferFactory createUnmodifiable() {
            return new UnmodifiableByteBufferFactory(new byte[0]);
        }

        @Override
        public Transmutable<UnmodifiableByteBufferFactory> recreate(
                UnmodifiableByteBufferFactory _unmodifiableByteBufferFactory) {
            _Durable newDurable = new _Durable();
            newDurable.unmodifiableByteBufferFactory = _unmodifiableByteBufferFactory;
            return newDurable;
        }
    }
}
