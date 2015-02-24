package org.agilewiki.jactor2.common.transmutable.tssmTransactions;

import org.agilewiki.jactor2.common.transmutable.transactions.SyncTransaction;
import org.agilewiki.jactor2.common.transmutable.transactions.Transaction;

import java.util.SortedMap;

/**
 * A TSSMap transaction.
 */
abstract public class TSSMTransaction<VALUE>
        extends SyncTransaction<SortedMap<String, VALUE>, TSSMap<VALUE>> {

    protected TSSMChangeManager<VALUE> tssmChangeManager;

    /**
     * Create a Transaction.
     */
    public TSSMTransaction() {
        super(null);
    }

    /**
     * Compose a Transaction.
     *
     * @param _parent The transaction to be applied before this one, or null.
     */
    public TSSMTransaction(final TSSMTransaction<VALUE> _parent) {
        super(_parent);
    }

    final protected void update(TSSMap<VALUE> transmutable)
            throws Exception {
        update();
    }

    abstract protected void update()
            throws Exception;

    public TSSMChangeManager<VALUE> getTSSMChangeManager() {
        return tssmChangeManager;
    }

    @Override
    protected void applySourceReference() {
        super.applySourceReference();
        tssmChangeManager = new TSSMChangeManager<VALUE>(transmutable);
    }

    @Override
    protected void applySourceTransaction(
            final Transaction<SortedMap<String, VALUE>, TSSMap<VALUE>> _transaction) {
        super.applySourceTransaction(_transaction);
        @SuppressWarnings("unchecked")
        final TSSMTransaction<VALUE> tssmTransaction = (TSSMTransaction<VALUE>) _transaction;
        tssmChangeManager = tssmTransaction.getTSSMChangeManager();
    }
}
