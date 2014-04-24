package org.agilewiki.jactor2.modules.transactions.properties;

import org.agilewiki.jactor2.modules.immutable.ImmutableProperties;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The change manager used by transactions to update the immutable properties map.
 */
public class PropertiesChangeManager implements AutoCloseable {

    ImmutableProperties immutableProperties;

    final TreeMap<String, PropertyChange> changes = new TreeMap<String, PropertyChange>();

    /**
     * An unmodifiable view of the property changes.
     */
    final public SortedMap<String, PropertyChange> readOnlyChanges = Collections.unmodifiableSortedMap(changes);

    private boolean closed;

    PropertiesChangeManager(final ImmutableProperties _immutableProperties) {
        immutableProperties = _immutableProperties;
    }

    /**
     * Returns the latest version of the immutable properties map.
     *
     * @return The latest version of the immutable properties map.
     */
    public ImmutableProperties getImmutableProperties() {
        return immutableProperties;
    }

    public void put(final String _key, final Boolean _newValue) {
        if (_newValue == null)
            put(_key, (String) null);
        else
            put(_key, _newValue ? "TRUE" : null);
    }

    /**
     * Update the immutable properties map.
     *
     * @param _key      The property name.
     * @param _newValue The new value of the property, or null.
     */
    public void put(final String _key, final String _newValue) {
        if (closed) {
            throw new IllegalStateException(
                    "Already closed, the transaction is complete.");
        }
        if (_key == null)
            throw new IllegalArgumentException("key may not be null");
        String oldValue = immutableProperties.get(_key);
        if (oldValue.equals(_newValue))
            return;
        if (_newValue == null)
            immutableProperties = immutableProperties.minus(_key);
        else
            immutableProperties = immutableProperties.plus(_key, _newValue);
        PropertyChange propertyChange = new PropertyChange(_key, oldValue, _newValue);
        changes.put(_key, propertyChange);
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
