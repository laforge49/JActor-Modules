package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

/**
 * A durable widget.
 */
public interface Durable
        extends Widget<UnmodifiableByteBufferFactory> {
    public DurableFactory getWidgetFactory();

    public UnmodifiableByteBufferFactory apply(final String _path,
                                               final String _params,
                                               final UnmodifiableByteBufferFactory _contentFactory);
}
