package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.core.blades.pubSub.RequestBus;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.Transaction;
import org.agilewiki.jactor2.core.blades.transmutable.transactions.TransmutableReference;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.requests.AOp;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.requests.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.durable.widgets.DurableImpl;

/**
 * Supports validation and notifications of changes to a DurableWidget.
 */
public class DurableReference
        extends TransmutableReference<UnmodifiableByteBufferFactory, DurableImpl> {

    /**
     * The RequestBus used to validate the changes made by a transaction.
     */
    public final RequestBus<DurableChanges> validationBus;

    /**
     * The RequestBus used to signal the changes made by a validated transaction.
     */
    public final RequestBus<DurableChanges> changeBus;

    public DurableReference(DurableImpl _transmutable) throws Exception {
        super(_transmutable);
        final NonBlockingReactor parentReactor = (NonBlockingReactor) getReactor()
                .getParentReactor();
        validationBus = new RequestBus<DurableChanges>(parentReactor);
        changeBus = new RequestBus<DurableChanges>(parentReactor);
    }

    public DurableReference(DurableImpl _transmutable, IsolationReactor _reactor) {
        super(_transmutable, _reactor);
        final NonBlockingReactor parentReactor = (NonBlockingReactor) getReactor()
                .getParentReactor();
        validationBus = new RequestBus<DurableChanges>(parentReactor);
        changeBus = new RequestBus<DurableChanges>(parentReactor);
    }

    public DurableReference(DurableImpl _transmutable, NonBlockingReactor _parentReactor) throws Exception {
        super(_transmutable, _parentReactor);
        validationBus = new RequestBus<DurableChanges>(_parentReactor);
        changeBus = new RequestBus<DurableChanges>(_parentReactor);
    }

    @Override
    public AOp<Void> applyAOp(final Transaction<UnmodifiableByteBufferFactory, DurableImpl> _durableTransaction) {
        return new AOp<Void>("apply", getReactor()) {
            private DurableChanges durableChanges;

            @Override
            protected void processAsyncOperation(final AsyncRequestImpl _asyncRequestImpl,
                                                 final AsyncResponseProcessor<Void> _asyncResponseProcessor)
                    throws Exception {
                final DurableTransaction durableTransaction = (DurableTransaction) _durableTransaction;


                final AsyncResponseProcessor<Void> validationResponseProcessor = new AsyncResponseProcessor<Void>() {
                    @Override
                    public void processAsyncResponse(final Void _response)
                            throws Exception {
                        durableTransaction.durableChangeManager.close();
                        updateUnmodifiable();
                        if (changeBus.noSubscriptions()) {
                            _asyncResponseProcessor.processAsyncResponse(null);
                        } else
                            _asyncRequestImpl.send(changeBus
                                            .sendsContentAOp(durableChanges),
                                    _asyncResponseProcessor, getTransmutable());
                    }
                };

                final AsyncResponseProcessor<Void> superResponseProcessor =
                        new AsyncResponseProcessor<Void>() {
                            @Override
                            public void processAsyncResponse(final Void _response)
                                    throws Exception {
                                durableChanges = durableTransaction.durableChangeManager.durableChanges();
                                if (validationBus.noSubscriptions()) {
                                    validationResponseProcessor.processAsyncResponse(null);
                                } else
                                    _asyncRequestImpl.send(validationBus
                                                    .sendsContentAOp(durableChanges),
                                            validationResponseProcessor);
                            }
                        };

                durableTransaction._eval(DurableReference.this, _asyncRequestImpl, superResponseProcessor);
            }
        };
    }
}
