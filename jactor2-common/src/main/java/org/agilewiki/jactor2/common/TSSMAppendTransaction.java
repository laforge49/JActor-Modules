package org.agilewiki.jactor2.common;

import org.agilewiki.jactor2.core.blades.transmutable.tssmTransactions.TSSMTransaction;
import org.agilewiki.jactor2.core.blades.transmutable.tssmTransactions.TSSMap;

import java.util.SortedMap;

/**
 * Appends to a list of values with a common prefix.
 *
 * @param <VALUE>    The type of value.
 */
public class TSSMAppendTransaction<VALUE> extends TSSMTransaction<VALUE> {

    public final String prefix;
    public final VALUE value;
    public final String sep = "!";

    /**
     * Create a Transaction.
     *
     * @param _prefix  The name.
     * @param _value The value.
     */
    public TSSMAppendTransaction(final String _prefix,
                                 final VALUE _value,
                                 final TSSMTransaction<VALUE> _parent) {
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
    public TSSMAppendTransaction(final String _prefix,
                                 final VALUE _value) {
        prefix = _prefix;
        value = _value;
    }

    @Override
    protected void update(final TSSMap<VALUE> _transmutable) throws Exception {
        SortedMap<String, VALUE> values = _transmutable.subMap(prefix + sep);
        int ndx;
        if (values.size() == 0) {
            ndx = 0;
        } else {
            String lastKey = values.lastKey();
            int i = lastKey.lastIndexOf(sep);
            ndx = new Integer(lastKey.substring(i + 1)).intValue() + 1;
        }
        String key = prefix + sep + ndx;
        tssmChangeManager.put(key, value);
    }

    @Override
    protected void updateTrace() {
        trace.insert(0, "\nTRACE: " + prefix + " += " + value);
    }
}
