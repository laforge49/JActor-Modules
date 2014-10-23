package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;

/**
 * Reflects a change made to a durable widget structure.
 */
public class DurableChange {
    public final String path;
    public final String params;
    public final String contentType;
    public final UnmodifiableByteBufferFactory newContentFactory;
    public final String trace;

    public DurableChange(final String _path,
                         final String _params,
                         final String _contentType,
                         final UnmodifiableByteBufferFactory _newContentFactory,
                         final String _trace) {
        path = _path;
        params = _params;
        contentType = _contentType;
        newContentFactory = _newContentFactory;
        trace = _trace;
    }
}
