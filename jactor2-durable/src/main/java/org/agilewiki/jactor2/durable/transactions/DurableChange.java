package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

/**
 * Reflects a change made to a durable widget structure.
 */
public class DurableChange {
    public final String path;
    public final String params;
    public final UnmodifiableByteBufferFactory oldContentFactory;
    public final UnmodifiableByteBufferFactory newContentFactory;

    public DurableChange (final String _path,
                          final String _params,
                          final UnmodifiableByteBufferFactory _oldContentFactory,
                          final UnmodifiableByteBufferFactory _newContentFactory) {
        path = _path;
        params = _params;
        oldContentFactory = _oldContentFactory;
        newContentFactory = _newContentFactory;
    }
}
