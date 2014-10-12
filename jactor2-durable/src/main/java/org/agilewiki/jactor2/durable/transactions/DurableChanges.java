package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;

import java.util.SortedMap;

/**
 * Content passed to the validation and change notification bus subscribers.
 */
public class DurableChanges {
    public final UnmodifiableByteBufferFactory durableWidgetBufferFactory;
    public final SortedMap<String, DurableChange> unmodifiableChanges;

    public DurableChanges(final UnmodifiableByteBufferFactory _durableWidgetBufferFactory,
                          final SortedMap<String, DurableChange> _unmodifiableChanges) {
        durableWidgetBufferFactory = _durableWidgetBufferFactory;
        unmodifiableChanges = _unmodifiableChanges;
    }
}
