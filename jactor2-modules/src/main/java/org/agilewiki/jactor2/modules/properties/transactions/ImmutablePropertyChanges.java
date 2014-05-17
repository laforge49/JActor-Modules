package org.agilewiki.jactor2.modules.properties.transactions;

import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;

import java.util.SortedMap;

/**
 * The content passed to subscribers of the validation and change RequestBus instances.
 */
public class ImmutablePropertyChanges {

    /**
     * The new version of the immutable properties map.
     */
    public final ImmutableProperties immutableProperties;

    /**
     * An unmodifiable sorted map of the property changes.
     */
    public final SortedMap<String, PropertyChange> readOnlyChanges;

    public ImmutablePropertyChanges(final PropertiesChangeManager propertiesChangeManager) {
        immutableProperties = propertiesChangeManager.getImmutableProperties();
        readOnlyChanges = propertiesChangeManager.readOnlyChanges;
    }
}
