package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.core.requests.SyncOperation;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.DurableWidget;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Change manager used by durable transaction to update a durable widget.
 */
public class DurableChangeManager implements AutoCloseable {
    private final DurableWidget durableWidget;
    final private TreeMap<String, DurableChange> changes = new TreeMap<String, DurableChange>();
    final private SortedMap<String, DurableChange> unmodifiableChanges = Collections.unmodifiableSortedMap(changes);
    private boolean closed;

    public DurableChangeManager(final DurableWidget _durableWidget) {
        durableWidget = _durableWidget;
    }

    public UnmodifiableByteBufferFactory apply(final String _path,
                                                                 final String _params,
                                                                 final UnmodifiableByteBufferFactory _contentFactory) {
        if (closed) {
            throw new IllegalStateException(
                    "Already closed, the transaction is complete.");
        }
        UnmodifiableByteBufferFactory oldContentFactory = durableWidget.apply(_path, _params, _contentFactory);
        DurableChange durableChange = new DurableChange(_path, _params, oldContentFactory, _contentFactory);
        changes.put(_path, durableChange);
        return oldContentFactory;
    }

    public DurableChanges durableChanges() {
        return new DurableChanges(durableWidget.createUnmodifiable(), unmodifiableChanges);
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
