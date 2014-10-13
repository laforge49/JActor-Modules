package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.DurableWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Change manager used by durable transaction to update a durable widget.
 */
public class DurableChangeManager implements AutoCloseable {
    private final DurableWidget durableWidget;
    final private List<DurableChange> changes = new ArrayList<DurableChange>();
    final private List<DurableChange> unmodifiableChanges = Collections.unmodifiableList(changes);
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
        changes.add(durableChange);
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
