package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

/**
 * A durable widget.
 */
public interface Durable
        extends Widget, Transmutable<UnmodifiableByteBufferFactory> {
    public UnmodifiableByteBufferFactory apply(final String _path,
                                               final String _params,
                                               final UnmodifiableByteBufferFactory _contentFactory);
}
