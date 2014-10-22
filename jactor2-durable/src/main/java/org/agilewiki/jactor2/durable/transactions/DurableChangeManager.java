package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.DurableImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Change manager used by durable transaction to update a durable widget.
 */
public class DurableChangeManager implements AutoCloseable {
    private final DurableImpl durableImpl;
    final private List<DurableChange> changes = new ArrayList<DurableChange>();
    final private List<DurableChange> unmodifiableChanges = Collections.unmodifiableList(changes);
    private boolean closed;

    public DurableChangeManager(final DurableImpl _durableImpl) {
        durableImpl = _durableImpl;
    }

    public String apply(final String _path,
                        final String _params,
                        final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception {
        if (closed) {
            throw new IllegalStateException(
                    "Already closed, the transaction is complete.");
        }
        DurableImpl._Durable did = (DurableImpl._Durable) durableImpl.asWidget().resolve(_path);
        String trace = did.apply(_params, _contentFactory);
        DurableChange durableChange = new DurableChange(_path, _params, _contentFactory, trace);
        changes.add(durableChange);
        return trace;
    }

    public DurableChanges durableChanges() {
        return new DurableChanges(durableImpl.createUnmodifiable(), unmodifiableChanges);
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
