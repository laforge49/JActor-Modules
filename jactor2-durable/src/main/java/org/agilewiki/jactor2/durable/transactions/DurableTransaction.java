package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.core.blades.transmutable.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.Transaction;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.Durable;

/**
 * A DurableWidget transaction.
 */
public class DurableTransaction
        extends SyncTransaction<UnmodifiableByteBufferFactory, Durable> {
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
    protected final void update(Durable transmutable) throws Exception {
        durableChangeManager.apply(path, params, contentFactory);
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
            final Transaction<UnmodifiableByteBufferFactory, Durable> _transaction) {
        super.applySourceTransaction(_transaction);
        @SuppressWarnings("unchecked")
        final DurableTransaction durableTransaction = (DurableTransaction) _transaction;
        durableChangeManager = durableTransaction.getDurableChangeManager();
    }
}
