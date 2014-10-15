package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;

import java.util.List;

/**
 * Content passed to the validation and change notification bus subscribers.
 */
public class DurableChanges {
    public final UnmodifiableByteBufferFactory durableWidgetBufferFactory;
    public final List<DurableChange> unmodifiableChanges;

    public DurableChanges(final UnmodifiableByteBufferFactory _durableWidgetBufferFactory,
                          final List<DurableChange> _unmodifiableChanges) {
        durableWidgetBufferFactory = _durableWidgetBufferFactory;
        unmodifiableChanges = _unmodifiableChanges;
    }
}
