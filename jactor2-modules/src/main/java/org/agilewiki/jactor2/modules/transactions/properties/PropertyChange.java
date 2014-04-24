package org.agilewiki.jactor2.modules.transactions.properties;

/**
 * Represents a change made to a property in the immutable properties map.
 */
public class PropertyChange {

    /**
     * The name of the property.
     */
    public final String name;

    /**
     * The old value of the property, or null.
     */
    public final String oldValue;

    /**
     * The new value of the property, or null.
     */
    public final String newValue;

    PropertyChange(final String _name, final String _oldValue,
                          final String _newValue) {
        name = _name;
        oldValue = _oldValue;
        newValue = _newValue;
    }
}
