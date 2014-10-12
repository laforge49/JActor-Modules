package org.agilewiki.jactor2.durable.widgets;

import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.core.blades.transmutable.Transmutable;
import org.agilewiki.jactor2.core.requests.SyncOperation;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

/**
 *  A durable widget.
 */
public interface DurableWidget
        extends Widget, Transmutable<UnmodifiableByteBufferFactory> {
    public SyncOperation<UnmodifiableByteBufferFactory> applySOp(final String _path,
                                        final String _params,
                                        final UnmodifiableByteBufferFactory _contentFactory);
}
