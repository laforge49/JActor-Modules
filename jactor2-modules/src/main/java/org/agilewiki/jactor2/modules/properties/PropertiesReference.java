package org.agilewiki.jactor2.modules.properties;

import org.agilewiki.jactor2.core.blades.transactions.ISMap;
import org.agilewiki.jactor2.core.blades.transactions.ImmutableReference;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.modules.pubSub.RequestBus;

import java.util.Map;

public class PropertiesReference extends ImmutableReference<ISMap<String>> implements PropertiesSource {

    public static ISMap<String> empty() {
        return Plant.createISMap();
    }

    public static ISMap<String> singleton(String key, String value) {
        return Plant.createISMap(key, value);
    }

    public static ISMap<String> from(Map<String, String> m) {
        return Plant.createISMap(m);
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
        this(empty());
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     */
    public PropertiesReference(final ISMap<String> _immutable) {
        super(_immutable);
        NonBlockingReactor parentReactor = (NonBlockingReactor) getReactor().getParentReactor();
        validationBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
        changeBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _reactor      The blade's reactor.
     */
    public PropertiesReference(final IsolationReactor _reactor) {
        this(empty(), _reactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     * @param _reactor      The blade's reactor.
     */
    public PropertiesReference(final ISMap<String> _immutable, final IsolationReactor _reactor) {
        super(_immutable, _reactor);
        NonBlockingReactor parentReactor = (NonBlockingReactor) _reactor.getParentReactor();
        validationBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
        changeBus = new RequestBus<ImmutablePropertyChanges>(parentReactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _parentReactor    The parent of the blade's reactor.
     */
    public PropertiesReference(final NonBlockingReactor _parentReactor) {
        this(empty(), _parentReactor);
    }

    /**
     * Create an ImmutableReference blade.
     *
     * @param _immutable    The immutable data structure to be operated on.
     * @param _parentReactor    The parent of the blade's reactor.
     */
    public PropertiesReference(final ISMap<String> _immutable, final NonBlockingReactor _parentReactor) {
        super(_immutable, _parentReactor);
        validationBus = new RequestBus<ImmutablePropertyChanges>(_parentReactor);
        changeBus = new RequestBus<ImmutablePropertyChanges>(_parentReactor);
    }

    public String toString() {
        return getImmutable().toString();
    }
}
