package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.CFacility;
import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

/**
 * Implements Durable as a nested class.
 */
public class DurableImpl extends WidgetImpl {

    public DurableImpl(final DurableFactory _widgetFactory,
                       final InternalWidget _parent,
                       final ByteBuffer _byteBuffer) {
        super(_widgetFactory, _parent, _byteBuffer);
    }

    public DurableImpl(final CFacility _facility,
                       final InternalWidget _parent) {
        super(DurableFactory.getFactory(_facility), _parent, null);
    }

    @Override
    public DurableFactory getInternalWidgetFactory() {
        return (DurableFactory) super.getInternalWidgetFactory();
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

    public String apply(final String _path,
                        final String _params,
                        final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception {
        throw new InvalidDurableParamsException(_params);
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(
            final UnmodifiableByteBufferFactory _unmodifiable) {
        return new DurableImpl(getInternalWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    public class _Durable extends _Widget implements Durable {
        @Override
        public _Durable resolve(final String _path) throws InvalidDurablePathException {
            if (_path.length() == 0)
                return this;
            throw new InvalidDurablePathException("Unsupported path: " + _path);
        }
    }
}
