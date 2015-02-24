package org.agilewiki.jactor2.common;

import junit.framework.TestCase;
import org.agilewiki.jactor2.common.pubSub.RequestBus;
import org.agilewiki.jactor2.common.pubSub.SubscribeAOp;
import org.agilewiki.jactor2.common.pubSub.Subscription;
import org.agilewiki.jactor2.core.impl.Plant;
import org.agilewiki.jactor2.core.messages.AsyncResponseProcessor;
import org.agilewiki.jactor2.core.messages.impl.AsyncRequestImpl;
import org.agilewiki.jactor2.core.reactors.IsolationReactor;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;

import java.util.concurrent.atomic.AtomicInteger;

public class PubSubTest extends TestCase {
    public void testI() throws Exception {
        System.out.println("I");
        new Plant();
        try {
            final IsolationReactor reactor = new IsolationReactor();
            final RequestBus<Void> requestBus = new RequestBus<Void>(reactor);
            requestBus.signalsContentSOp(null).call();
            final Subscription<Void> s1 = new SubscribeAOp<Void>(
                    requestBus, reactor) {
                @Override
                protected void processContent(final Void _content,
                                              AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<Void> _asyncResponseProcessor)
                        throws Exception {
                    System.out.println("ping");
                    _asyncResponseProcessor.processAsyncResponse(null);
                }
            }.call();
            requestBus.signalsContentSOp(null).call();
            s1.unsubscribe();
            requestBus.signalsContentSOp(null).call();
        } finally {
            Plant.close();
        }
    }

    public void testJ() throws Exception {
        System.out.println("J");
        new Plant();
        try {
            final AtomicInteger counter = new AtomicInteger();
            final IsolationReactor busReactor = new IsolationReactor();
            final IsolationReactor subscriberReactor = new IsolationReactor();
            final RequestBus<Void> requestBus = new RequestBus<Void>(busReactor);
            requestBus.sendsContentAOp(null).call();
            assertEquals(counter.get(), 0);
            new SubscribeAOp<Void>(requestBus, subscriberReactor) {
                @Override
                protected void processContent(final Void _content,
                                              AsyncRequestImpl _asyncRequestImpl,
                                              AsyncResponseProcessor<Void> _asyncResponseProcessor)
                        throws Exception {
                    System.out.println("ping");
                    counter.incrementAndGet();
                    _asyncResponseProcessor.processAsyncResponse(null);
                }
            }.call();
            requestBus.sendsContentAOp(null).call();
            assertEquals(counter.get(), 1);
            subscriberReactor.close();
            try {
                requestBus.sendsContentAOp(null).call();
            } catch (final ReactorClosedException e) {
            }
            assertEquals(counter.get(), 1);
        } finally {
            Plant.close();
        }
    }
}
