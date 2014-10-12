package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.core.blades.pubSub.RequestBus;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.TransmutableReference;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.durable.transmutableBuffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.DurableWidget;

/**
 * Supports validation and notifications of changes to a DurableWidget.
 */
public class DurableReference extends TransmutableReference<UnmodifiableByteBufferFactory, DurableWidget> {

    /**
     * The RequestBus used to validate the changes made by a transaction.
     */
    public final RequestBus<DurableChanges> validationBus;

    /**
     * The RequestBus used to signal the changes made by a validated transaction.
     */
    public final RequestBus<DurableChanges> changeBus;

    public DurableReference(DurableWidget _transmutable) throws Exception {
        super(_transmutable);
        final NonBlockingReactor parentReactor = (NonBlockingReactor) getReactor()
                .getParentReactor();
        validationBus = new RequestBus<DurableChanges>(parentReactor);
        changeBus = new RequestBus<DurableChanges>(parentReactor);
    }

    public DurableReference(DurableWidget _transmutable, IsolationReactor _reactor) {
        super(_transmutable, _reactor);
        final NonBlockingReactor parentReactor = (NonBlockingReactor) getReactor()
                .getParentReactor();
        validationBus = new RequestBus<DurableChanges>(parentReactor);
        changeBus = new RequestBus<DurableChanges>(parentReactor);
    }

    public DurableReference(DurableWidget _transmutable, NonBlockingReactor _parentReactor) throws Exception {
        super(_transmutable, _parentReactor);
        validationBus = new RequestBus<DurableChanges>(_parentReactor);
        changeBus = new RequestBus<DurableChanges>(_parentReactor);
    }
}
