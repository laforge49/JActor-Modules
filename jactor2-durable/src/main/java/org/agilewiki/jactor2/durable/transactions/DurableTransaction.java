package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.Transaction;

/**
 * A DurableWidget transaction.
 */
public class DurableTransaction
        extends SyncTransaction<UnmodifiableByteBufferFactory, Widget> {
    protected DurableChangeManager durableChangeManager;
    protected final String path;
    protected final String params;
    protected final String contentType;
    protected final UnmodifiableByteBufferFactory contentFactory;

    public DurableTransaction(final String _path,
                              final String _params,
                              final Widget _widget) {
        this(_path, _params, _widget, null);
    }

    public DurableTransaction(final String _path,
                              final String _params,
                              final Widget _widget,
                              final DurableTransaction _parent) {
        super(_parent);
        path = _path;
        params = _params;
        contentType = _widget.getWidgetFactory().getFactoryKey();
        contentFactory = _widget.createUnmodifiable();
    }

    @Override
    protected final void update(Widget transmutable) throws Exception {
        String result = durableChangeManager.apply(path, params, contentType, contentFactory);
        if (result != null)
            trace.insert(0, "\nRESULT; " + result);
    }

    @Override
    public void updateTrace() {
        trace.insert(0, "\nTRACE: " + path + "  " + params);
    }

    public DurableChangeManager getDurableChangeManager() {
        return durableChangeManager;
    }

    @Override
    protected void applySourceReference() {
        super.applySourceReference();
        durableChangeManager = new DurableChangeManager(transmutable);
    }

    @Override
    protected void applySourceTransaction(
            final Transaction<UnmodifiableByteBufferFactory, Widget> _transaction) {
        super.applySourceTransaction(_transaction);
        @SuppressWarnings("unchecked")
        final DurableTransaction durableTransaction = (DurableTransaction) _transaction;
        durableChangeManager = durableTransaction.getDurableChangeManager();
    }
}
