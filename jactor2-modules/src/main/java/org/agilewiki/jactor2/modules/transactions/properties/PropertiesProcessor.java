package org.agilewiki.jactor2.modules.transactions.properties;

import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.modules.properties.immutable.HashTreePProperties;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;
import org.agilewiki.jactor2.modules.properties.transactions.ImmutablePropertyChanges;
import org.agilewiki.jactor2.modules.properties.transactions.PropertiesChangeManager;
import org.agilewiki.jactor2.modules.transactions.TransactionProcessor;

import java.util.Map;

/**
 * Transaction-based updates to an immutable properties map.
 */
public class PropertiesProcessor
        extends TransactionProcessor<PropertiesChangeManager, ImmutableProperties, ImmutablePropertyChanges> {

    static ImmutableProperties empty() {
        return HashTreePProperties.empty();
    }

    static ImmutableProperties singleton(String key, String value) {
        return HashTreePProperties.singleton(key, value);
    }

    static ImmutableProperties from(Map<String, String> m) {
        return HashTreePProperties.from(m);
    }

    PropertiesChangeManager propertiesChangeManager;

    public PropertiesProcessor(final NonBlockingReactor _parentReactor) {
        super(_parentReactor, empty());
    }

    public PropertiesProcessor(final NonBlockingReactor _parentReactor,
                               Map<String, String> _initialState) {
        super(_parentReactor, from(_initialState));
    }

    @Override
    protected PropertiesChangeManager newChangeManager() {
        propertiesChangeManager = new PropertiesChangeManager(immutableState);
        return propertiesChangeManager;
    }

    @Override
    protected ImmutablePropertyChanges newChanges() {
        return new ImmutablePropertyChanges(propertiesChangeManager);
    }

    @Override
    protected void newImmutableState() {
        immutableState = propertiesChangeManager.getImmutableProperties();
    }

    /**
     * A transactional put request.
     *
     * @param _key      The property name.
     * @param _newValue The new value.
     * @return The request.
     */
    public AsyncRequest<Void> putAReq(final String _key, final String _newValue) {
        return new PropertiesTransactionAReq(parentReactor, this) {
            protected void update(final PropertiesChangeManager _changeManager) {
                _changeManager.put(_key, _newValue);
            }
        };
    }

    /**
     * A transactional put request.
     *
     * @param _key      The property name.
     * @param _newValue The new value.
     * @return The request.
     */
    public AsyncRequest<Void> putAReq(final String _key, final Boolean _newValue) {
        return new PropertiesTransactionAReq(parentReactor, this) {
            protected void update(final PropertiesChangeManager _changeManager) {
                _changeManager.put(_key, _newValue);
            }
        };
    }

    /**
     * A transactional compare and set request.
     * A put is performed only if the old value was equal to the expected value.
     *
     * @param _key           The property name.
     * @param _expectedValue The new value.
     * @param _newValue      The new value.
     * @return The request.
     */
    public AsyncRequest<Void> compareAndSetAReq(final String _key, final Boolean _expectedValue, final Boolean _newValue) {
        return new PropertiesTransactionAReq(parentReactor, this) {
            protected void update(final PropertiesChangeManager _changeManager) {
                boolean oldValue = _changeManager.getImmutableProperties().containsValue(_key);
                boolean expectedValue = _expectedValue == null ? false : _expectedValue;
                if ((oldValue == expectedValue)) {
                    _changeManager.put(_key, _newValue);
                }
            }
        };
    }

    /**
     * A transactional compare and set request.
     * A put is performed only if the old value was equal to the expected value.
     *
     * @param _key           The property name.
     * @param _expectedValue The new value.
     * @param _newValue      The new value.
     * @return The request.
     */
    public AsyncRequest<Void> compareAndSetAReq(final String _key, final String _expectedValue, final String _newValue) {
        return new PropertiesTransactionAReq(parentReactor, this) {
            protected void update(final PropertiesChangeManager _changeManager) {
                String oldValue = _changeManager.getImmutableProperties().get(_key);
                if ((oldValue != null && oldValue.equals(_expectedValue) ||
                        (oldValue == null && _expectedValue == null))) {
                    _changeManager.put(_key, _newValue);
                }
            }
        };
    }

    public String toString() {
        return getImmutableState().toString();
    }
}
