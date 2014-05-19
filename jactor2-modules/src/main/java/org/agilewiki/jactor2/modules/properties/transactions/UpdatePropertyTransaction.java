package org.agilewiki.jactor2.modules.properties.transactions;

import org.agilewiki.jactor2.core.blades.transactions.ImmutableSource;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;

/**
 * Composable transaction to update a property.
 */
public class UpdatePropertyTransaction extends PropertyTransaction {

    private final String propertyName;

    private final String propertyValue;

    private final boolean expecting;

    private final String expectedValue;

    /**
     * Create a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final String _propertyValue) {
        propertyName = _propertyName;
        propertyValue = _propertyValue;
        expecting = false;
        expectedValue = null;
    }

    /**
     * Create a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     * @param _expectedValue The expected value of the property, or null.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final String _propertyValue,
                                     final String _expectedValue) {
        propertyName = _propertyName;
        propertyValue = _propertyValue;
        expecting = true;
        expectedValue = _expectedValue;
    }

    /**
     * Create a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final boolean _propertyValue) {
        propertyName = _propertyName;
        propertyValue = _propertyValue ? "TRUE" : null;
        expecting = false;
        expectedValue = null;
    }

    /**
     * Create a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property.
     * @param _expectedValue The expected value of the property.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final boolean _propertyValue,
                                     final boolean _expectedValue) {
        propertyName = _propertyName;
        propertyValue = _propertyValue ? "TRUE" : null;
        expecting = true;
        expectedValue = _expectedValue ? "TRUE" : null;
    }

    /**
     * Compose a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     * @param _parent        The property transaction to be applied before this one.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final String _propertyValue,
                                     final PropertyTransaction _parent) {
        super(_parent);
        propertyName = _propertyName;
        propertyValue = _propertyValue;
        expecting = false;
        expectedValue = null;
    }

    /**
     * Compose a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property, or null.
     * @param _expectedValue The expected value of the property, or null.
     * @param _parent        The property transaction to be applied before this one.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final String _propertyValue,
                                     final String _expectedValue,
                                     final PropertyTransaction _parent) {
        super(_parent);
        propertyName = _propertyName;
        propertyValue = _propertyValue;
        expecting = true;
        expectedValue = _expectedValue;
    }

    /**
     * Compose a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property.
     * @param _parent        The property transaction to be applied before this one.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final boolean _propertyValue,
                                     final PropertyTransaction _parent) {
        super(_parent);
        propertyName = _propertyName;
        propertyValue = _propertyValue ? "TRUE" : null;
        expecting = false;
        expectedValue = null;
    }

    /**
     * Compose a Transaction to update a property.
     *
     * @param _propertyName  The name of the property.
     * @param _propertyValue The value of the property.
     * @param _expectedValue The expected value of the property.
     * @param _parent        The property transaction to be applied before this one.
     */
    public UpdatePropertyTransaction(final String _propertyName,
                                     final boolean _propertyValue,
                                     final boolean _expectedValue,
                                     final PropertyTransaction _parent) {
        super(_parent);
        propertyName = _propertyName;
        propertyValue = _propertyValue ? "TRUE" : null;
        expecting = true;
        expectedValue = _expectedValue ? "TRUE" : null;
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
    protected boolean precheck(final ImmutableProperties _immutableProperties) {
        String old = _immutableProperties.get(propertyName);
        return !expecting || (expectedValue == null && old == null) ||
                (expectedValue != null && expectedValue.equals(old));
    }

    @Override
    protected void updateTrace() {
        trace.insert(0, "\nTRACE: " + propertyName + " = " + propertyValue);
    }
}
