package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.core.blades.ismTransactions.ISMSyncTransaction;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMTransaction;
import org.agilewiki.jactor2.core.blades.ismTransactions.ISMap;
import org.agilewiki.jactor2.core.blades.transactions.ImmutableSource;

/**
 * Appends to a list of values with a common prefix.
 *
 * @param <VALUE>    The type of value.
 */
public class ISMAppendTransaction<VALUE> extends ISMSyncTransaction<VALUE> {

    public final String prefix;
    public final VALUE value;
    public final String sep = "!";

    /**
     * Create a Transaction.
     *
     * @param _prefix  The name.
     * @param _value The value.
     */
    public ISMAppendTransaction(final String _prefix,
                                final VALUE _value,
                                final ISMTransaction<VALUE> _parent) {
        super(_parent);
        prefix = _prefix;
        value = _value;
    }

    /**
     * Create a Transaction.
     *
     * @param _prefix  The name.
     * @param _value The value.
     */
    public ISMAppendTransaction(final String _prefix,
                                final VALUE _value) {
        prefix = _prefix;
        value = _value;
    }

    @Override
    protected void update(ImmutableSource<ISMap<VALUE>> source) throws Exception {
        ISMap<VALUE> values = source.getImmutable().subMap(prefix + sep);
        String lastKey = values.sortedKeySet().last();
        int i = lastKey.lastIndexOf(sep);
        int ndx = new Integer(lastKey.substring(i + 1)).intValue() + 1;
        String key = prefix + sep + ndx;
        immutable = source.getImmutable().plus(key, value);
        immutableChangeManager.put(key, value);
    }

    @Override
    protected void updateTrace() {
        trace.insert(0, "\nTRACE: " + prefix + " += " + value);
    }
}
