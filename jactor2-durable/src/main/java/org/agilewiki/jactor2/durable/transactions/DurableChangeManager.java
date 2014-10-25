package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.InvalidWidgetPathException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Change manager used by durable transaction to update a widget.
 */
public class DurableChangeManager implements AutoCloseable {
    private final Widget widget;
    final private List<DurableChange> changes = new ArrayList<DurableChange>();
    final private List<DurableChange> unmodifiableChanges = Collections.unmodifiableList(changes);
    private boolean closed;

    public DurableChangeManager(final Widget _widget) {
        widget = _widget;
    }

    public String apply(final String _path,
                        final String _params,
                        final String _contentType,
                        final UnmodifiableByteBufferFactory _contentFactory)
            throws Exception {
        if (closed) {
            throw new IllegalStateException(
                    "Already closed, the transaction is complete.");
        }
        Widget widget = this.widget.resolve(_path);
        if (widget == null)
            throw new InvalidWidgetPathException(_path);
        String trace = widget.apply(_params, _contentType, _contentFactory);
        DurableChange durableChange = new DurableChange(_path, _params, _contentType, _contentFactory, trace);
        changes.add(durableChange);
        return trace;
    }

    public DurableChanges durableChanges() {
        return new DurableChanges(widget.createUnmodifiable(), unmodifiableChanges);
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
