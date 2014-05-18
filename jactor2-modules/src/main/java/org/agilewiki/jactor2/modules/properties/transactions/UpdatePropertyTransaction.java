package org.agilewiki.jactor2.modules.properties.transactions;

import org.agilewiki.jactor2.core.blades.transactions.ImmutableSource;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;

/**
 * Composable transaction to update a property.
 */
public class UpdatePropertyTransaction extends PropertyTransaction {

    private final String propertyName;

    private final String propertyValue;

    /**
     * Create a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     */
    public UpdatePropertyTransaction(final String _propertyName, final String _propertyValue) {
        super(null);
        propertyName = _propertyName;
        propertyValue = _propertyValue;
    }

    /**
     * Create a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     */
    public UpdatePropertyTransaction(final String _propertyName, final boolean _propertyValue) {
        super(null);
        propertyName = _propertyName;
        propertyValue = _propertyValue ? "TRUE" : null;
    }

    /**
     * Compose a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     * @param _parent        The property transaction to be applied before this one.
     */
    public UpdatePropertyTransaction(final String _propertyName, final String _propertyValue,
                                     final PropertyTransaction _parent) {
        super(_parent);
        propertyName = _propertyName;
        propertyValue = _propertyValue;
    }

    /**
     * Compose a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     * @param _parent        The property transaction to be applied before this one.
     */
    public UpdatePropertyTransaction(final String _propertyName, final boolean _propertyValue,
                                     final PropertyTransaction _parent) {
        super(_parent);
        propertyName = _propertyName;
        propertyValue = _propertyValue ? "TRUE" : null;
    }

    /**
     * Updates the immutable data structure.
     *
     * @param source The Transaction or ImmutableReference holding the immutable to be operated on.
     */
    @Override
    protected void update(ImmutableSource<ImmutableProperties> source) throws Exception {
        if (propertyValue == null)
            immutable = source.getImmutable().minus(propertyName);
        else
            immutable = source.getImmutable().plus(propertyName, propertyValue);
        propertiesChangeManager.put(propertyName, propertyValue);
    }

    @Override
    protected void updateTrace() {
        trace.insert(0, "\nTRACE: " + propertyName + " = " + propertyValue);
    }
}
