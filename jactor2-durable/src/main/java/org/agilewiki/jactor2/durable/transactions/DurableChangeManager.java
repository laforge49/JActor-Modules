package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.Durable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Change manager used by durable transaction to update a durable widget.
 */
public class DurableChangeManager implements AutoCloseable {
    private final Durable durable;
    final private List<DurableChange> changes = new ArrayList<DurableChange>();
    final private List<DurableChange> unmodifiableChanges = Collections.unmodifiableList(changes);
    private boolean closed;

    public DurableChangeManager(final Durable _durable) {
        durable = _durable;
    }

    public UnmodifiableByteBufferFactory apply(final String _path,
                                               final String _params,
                                               final UnmodifiableByteBufferFactory _contentFactory) {
        if (closed) {
            throw new IllegalStateException(
                    "Already closed, the transaction is complete.");
        }
        UnmodifiableByteBufferFactory resultFactory = durable.apply(_path, _params, _contentFactory);
        DurableChange durableChange = new DurableChange(_path, _params, resultFactory, _contentFactory);
        changes.add(durableChange);
        return resultFactory;
    }

    public DurableChanges durableChanges() {
        return new DurableChanges(durable.createUnmodifiable(), unmodifiableChanges);
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
