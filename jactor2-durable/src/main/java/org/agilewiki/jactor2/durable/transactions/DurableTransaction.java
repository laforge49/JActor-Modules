package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.core.blades.transmutable.transactions.SyncTransaction;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.Transaction;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.Durable;

/**
 * A DurableWidget transaction.
 */
public abstract class DurableTransaction
        extends SyncTransaction<UnmodifiableByteBufferFactory, Durable> {
    protected DurableChangeManager durableChangeManager;

    public DurableTransaction() {
    }

    public DurableTransaction(final DurableTransaction _parent) {
        super(_parent);
    }

    @Override
    protected final void update(Durable transmutable) throws Exception {
        update();
    }

    abstract protected void update()
            throws Exception;

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
