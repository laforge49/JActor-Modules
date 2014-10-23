package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.widgets.WidgetImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.Transaction;

/**
 * A DurableWidget transaction.
 */
public class DurableTransaction
        extends SyncTransaction<UnmodifiableByteBufferFactory, WidgetImpl> {
    protected DurableChangeManager durableChangeManager;
    protected final String path;
    protected final String params;
    protected final UnmodifiableByteBufferFactory contentFactory;

    public DurableTransaction(final String _path,
                              final String _params,
                              final UnmodifiableByteBufferFactory _contentFactory) {
        path = _path;
        params = _params;
        contentFactory = _contentFactory;
    }

    public DurableTransaction(final String _path,
                              final String _params,
                              final UnmodifiableByteBufferFactory _contentFactory,
                              final DurableTransaction _parent) {
        super(_parent);
        path = _path;
        params = _params;
        contentFactory = _contentFactory;
    }

    @Override
    protected final void update(WidgetImpl transmutable) throws Exception {
        String result = durableChangeManager.apply(path, params, contentFactory);
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
            final Transaction<UnmodifiableByteBufferFactory, WidgetImpl> _transaction) {
        super.applySourceTransaction(_transaction);
        @SuppressWarnings("unchecked")
        final DurableTransaction durableTransaction = (DurableTransaction) _transaction;
        durableChangeManager = durableTransaction.getDurableChangeManager();
    }
}
