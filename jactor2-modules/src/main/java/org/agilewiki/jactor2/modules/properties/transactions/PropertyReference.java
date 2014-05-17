package org.agilewiki.jactor2.modules.properties.transactions;

import org.agilewiki.jactor2.core.blades.transactions.ImmutableReference;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.modules.properties.immutable.HashTreePProperties;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;

import java.util.Map;

public class PropertyReference extends ImmutableReference<ImmutableProperties> {

    public static ImmutableProperties empty() {
        return HashTreePProperties.empty();
    }

    public static ImmutableProperties singleton(String key, String value) {
        return HashTreePProperties.singleton(key, value);
    }

    public static ImmutableProperties from(Map<String, String> m) {
        return HashTreePProperties.from(m);
    }

    PropertiesChangeManager propertiesChangeManager;

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     */
    public PropertyReference(final ImmutableProperties _immutable) {
        super(_immutable);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     * @param _reactor      The blade's reactor.
     */
    public PropertyReference(final ImmutableProperties _immutable, final IsolationReactor _reactor) {
        super(_immutable, _reactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     * @param _parentReactor    The parent of the blade's reactor.
     */
    public PropertyReference(final ImmutableProperties _immutable, final NonBlockingReactor _parentReactor) {
        super(_immutable, _parentReactor);
    }

    public String toString() {
        return getImmutable().toString();
    }
}
