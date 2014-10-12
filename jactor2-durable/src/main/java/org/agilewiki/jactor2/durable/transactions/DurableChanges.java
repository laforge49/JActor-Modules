package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

import java.util.SortedMap;

/**
 * Content passed to the validation and change notification bus subscribers.
 */
public class DurableChanges {
    public final UnmodifiableByteBufferFactory stateFactory;
    public final SortedMap<String, DurableChange> unmodifiableChanges;

    public DurableChanges(final UnmodifiableByteBufferFactory _stateFactory,
                          final SortedMap<String, DurableChange> _unmodifiableChanges) {
        stateFactory = _stateFactory;
        unmodifiableChanges = _unmodifiableChanges;
    }
}
