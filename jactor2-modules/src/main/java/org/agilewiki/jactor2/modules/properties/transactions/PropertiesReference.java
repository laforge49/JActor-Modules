package org.agilewiki.jactor2.modules.properties.transactions;

import org.agilewiki.jactor2.core.blades.transactions.ImmutableReference;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.modules.properties.immutable.HashTreePProperties;
import org.agilewiki.jactor2.modules.properties.immutable.ImmutableProperties;
import org.agilewiki.jactor2.modules.pubSub.RequestBus;

import java.util.Map;

public class PropertiesReference extends ImmutableReference<ImmutableProperties> implements PropertiesSource {

    public static ImmutableProperties empty() {
        return HashTreePProperties.empty();
    }

    public static ImmutableProperties singleton(String key, String value) {
        return HashTreePProperties.singleton(key, value);
    }

    public static ImmutableProperties from(Map<String, String> m) {
        return HashTreePProperties.from(m);
    }

    /**
     * The RequestBus used to validate the changes made by a transaction.
     */
    public final RequestBus<ImmutablePropertyChanges> validationBus;

    /**
     * The RequestBus used to signal the changes made by a validated transaction.
     */
    public final RequestBus<ImmutablePropertyChanges> changeBus;

    public PropertiesReference() {
        super(empty());
        NonBlockingReactor parentReactor = (NonBlockingReactor) getReactor().getParentReactor();
        validationBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
        changeBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     */
    public PropertiesReference(final ImmutableProperties _immutable) {
        super(_immutable);
        NonBlockingReactor parentReactor = (NonBlockingReactor) getReactor().getParentReactor();
        validationBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
        changeBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     * @param _reactor      The blade's reactor.
     */
    public PropertiesReference(final ImmutableProperties _immutable, final IsolationReactor _reactor) {
        super(_immutable, _reactor);
        NonBlockingReactor parentReactor = (NonBlockingReactor) _reactor.getParentReactor();
        validationBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
        changeBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     * @param _parentReactor    The parent of the blade's reactor.
     */
    public PropertiesReference(final ImmutableProperties _immutable, final NonBlockingReactor _parentReactor) {
        super(_immutable, _parentReactor);
        validationBus = new RequestBus<ImmutablePropertyChanges>(_parentReactor);
        changeBus = new RequestBus<ImmutablePropertyChanges>(_parentReactor);
    }

    public String toString() {
        return getImmutable().toString();
    }
}
