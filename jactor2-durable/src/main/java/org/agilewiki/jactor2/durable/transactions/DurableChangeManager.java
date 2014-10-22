package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.widgets.InternalWidget;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.DurableImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Change manager used by durable transaction to update a durable widget.
 */
public class DurableChangeManager implements AutoCloseable {
    private final InternalWidget internalWidget;
    final private List<DurableChange> changes = new ArrayList<DurableChange>();
    final private List<DurableChange> unmodifiableChanges = Collections.unmodifiableList(changes);
    private boolean closed;

    public DurableChangeManager(final InternalWidget _internalWidget) {
        internalWidget = _internalWidget;
    }

    public String apply(final String _path,
                        final String _params,
                        final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception {
        if (closed) {
            throw new IllegalStateException(
                    "Already closed, the transaction is complete.");
        }
        WidgetImpl._Widget widget = internalWidget.asWidget().resolve(_path);
        String trace = widget.apply(_params, _contentFactory);
        DurableChange durableChange = new DurableChange(_path, _params, _contentFactory, trace);
        changes.add(durableChange);
        return trace;
    }

    public DurableChanges durableChanges() {
        return new DurableChanges(internalWidget.createUnmodifiable(), unmodifiableChanges);
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
