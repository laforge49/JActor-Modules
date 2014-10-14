package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.WidgetFactory;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

/**
 * Implements Durable as a nested class.
 */
public class DurableImpl extends WidgetImpl {
    public DurableImpl(WidgetFactory _widgetFactory, DurableImpl _parent) {
        super(_widgetFactory, _parent);
    }

    public WidgetFactory getWidgetFactory() {
        return super.getWidgetFactory();
    }

    public DurableImpl getParent() {
        return (DurableImpl) super.getParent();
    }

    public Durable asWidget() {
        return  (Durable) super.asWidget();
    }

    protected Durable newWidget() {
        return new _Durable();
    }

    protected class _Durable extends _Widget implements Durable {

        @Override
        public UnmodifiableByteBufferFactory apply(String _path, String _params, UnmodifiableByteBufferFactory _contentFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public UnmodifiableByteBufferFactory createUnmodifiable() {
            return new UnmodifiableByteBufferFactory(new byte[0]);
        }

        @Override
        public Transmutable<UnmodifiableByteBufferFactory> recreate(UnmodifiableByteBufferFactory unmodifiable) {
            return new _Durable();
        }
    }
}
