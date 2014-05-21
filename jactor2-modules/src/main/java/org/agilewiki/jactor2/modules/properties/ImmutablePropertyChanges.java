package org.agilewiki.jactor2.modules.properties;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;

import java.util.SortedMap;

/**
 * The content passed to subscribers of the validation and change RequestBus instances.
 */
public class ImmutablePropertyChanges {

    /**
     * The new version of the immutable properties map.
     */
    public final ISMap<String> immutableProperties;

    /**
     * An unmodifiable sorted map of the property changes.
     */
    public final SortedMap<String, PropertyChange> readOnlyChanges;

    public ImmutablePropertyChanges(final PropertiesChangeManager propertiesChangeManager) {
        immutableProperties = propertiesChangeManager.getImmutableProperties();
        readOnlyChanges = propertiesChangeManager.readOnlyChanges;
    }
}
