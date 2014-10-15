package org.agilewiki.jactor2.common.widgets;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;

import java.nio.ByteBuffer;

/**
 * Implements Widget as a nested class.
 */
public class WidgetImpl implements Transmutable<UnmodifiableByteBufferFactory> {
    private final WidgetFactory widgetFactory;
    private final _Widget widget;
    private final WidgetImpl parent;
    protected final ByteBuffer byteBuffer;

    public WidgetImpl(final WidgetFactory _widgetFactory,
                      final WidgetImpl _parent,
                      ByteBuffer _byteBuffer) {
        widgetFactory = _widgetFactory;
        parent = _parent;
        byteBuffer = _byteBuffer;
        widget = newWidget();
    }

    public WidgetFactory getWidgetFactory() {
        return widgetFactory;
    }

    public WidgetImpl getParent() {
        return parent;
    }

    public _Widget asWidget() {
        return widget;
    }

    protected _Widget newWidget() {
        return new _Widget();
    }

    @Override
    public UnmodifiableByteBufferFactory createUnmodifiable() {
        return new UnmodifiableByteBufferFactory(byteBuffer.array());
    }

    @Override
    public Transmutable<UnmodifiableByteBufferFactory> recreate(final UnmodifiableByteBufferFactory _unmodifiable) {
        return new WidgetImpl(getWidgetFactory(),
                getParent(), _unmodifiable.duplicateByteBuffer());
    }

    protected class _Widget implements Widget {
    }
}
