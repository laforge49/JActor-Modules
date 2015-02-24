package org.agilewiki.jactor2.durable.transactions;

import org.agilewiki.jactor2.common.pubSub.RequestBus;
import org.agilewiki.jactor2.common.transmutable.transactions.Transaction;
import org.agilewiki.jactor2.common.transmutable.transactions.TransmutableReference;
import org.agilewiki.jactor2.common.widgets.Widget;
import org.agilewiki.jactor2.common.widgets.buffers.UnmodifiableByteBufferFactory;
import org.agilewiki.jactor2.core.messages.AOp;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;

/**
 * Supports validation and notifications of changes to a DurableWidget.
 */
public class DurableReference
        extends TransmutableReference<UnmodifiableByteBufferFactory, Widget> {

    /**
     * The RequestBus used to validate the changes made by a transaction.
     */
    public final RequestBus<DurableChanges> validationBus;

    /**
     * The RequestBus used to signal the changes made by a validated transaction.
     */
    public final RequestBus<DurableChanges> changeBus;

    public DurableReference(Widget _transmutable) throws Exception {
        super(_transmutable);
        final IsolationReactor reactor = getReactor();
        validationBus = new RequestBus<DurableChanges>(reactor);
        changeBus = new RequestBus<DurableChanges>(reactor);
    }

    public DurableReference(Widget _transmutable, IsolationReactor _reactor) {
        super(_transmutable, _reactor);
        validationBus = new RequestBus<DurableChanges>(_reactor);
        changeBus = new RequestBus<DurableChanges>(_reactor);
    }

    @Override
    public AOp<Void> applyAOp(final Transaction<UnmodifiableByteBufferFactory, Widget> _durableTransaction) {
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
