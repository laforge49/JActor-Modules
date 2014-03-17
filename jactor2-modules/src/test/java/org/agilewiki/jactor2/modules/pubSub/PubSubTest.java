package org.agilewiki.jactor2.modules.pubSub;

import junit.framework.TestCase;
import org.agilewiki.jactor2.core.plant.Plant;
import org.agilewiki.jactor2.core.reactors.CommonReactor;
import org.agilewiki.jactor2.core.reactors.NonBlockingReactor;
import org.agilewiki.jactor2.core.reactors.ReactorClosedException;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.modules.MPlant;

import java.util.concurrent.atomic.AtomicInteger;

public class PubSubTest extends TestCase {
    public void testI() throws Exception {
        System.out.println("I");
        new MPlant();
        try {
            final NonBlockingReactor reactor = new NonBlockingReactor();
            final RequestBus<Void> requestBus = new RequestBus<Void>(reactor);
            requestBus.signalsContentSReq(null).call();
            final Subscription<Void> s1 = new SubscribeAReq<Void>(requestBus,
                    reactor) {
                @Override
                protected void processContent(
                        final Void _content,
                        final AsyncRequest<Void> _asyncRequest)
                        throws Exception {
                    System.out.println("ping");
                    _asyncRequest.processAsyncResponse(null);
                }
            }.call();
            requestBus.signalsContentSReq(null).call();
            s1.unsubscribe();
            requestBus.signalsContentSReq(null).call();
        } finally {
            Plant.close();
        }
    }

    public void testJ() throws Exception {
        System.out.println("J");
        new MPlant();
        try {
            final AtomicInteger counter = new AtomicInteger();
            final NonBlockingReactor busReactor = new NonBlockingReactor();
            final CommonReactor subscriberReactor = new NonBlockingReactor();
            final RequestBus<Void> requestBus = new RequestBus<Void>(busReactor);
            requestBus.sendsContentAReq(null).call();
            assertEquals(counter.get(), 0);
            final Subscription<Void> s1 = new SubscribeAReq<Void>(requestBus,
                    subscriberReactor) {
                @Override
                protected void processContent(
                        final Void _content,
                        final AsyncRequest<Void> _asyncRequest) {
                    System.out.println("ping");
                    counter.incrementAndGet();
                    _asyncRequest.processAsyncResponse(null);
                }
            }.call();
            requestBus.sendsContentAReq(null).call();
            assertEquals(counter.get(), 1);
            subscriberReactor.close();
            try {
             requestBus.sendsContentAReq(null).call();
            } catch (ReactorClosedException e) {
            }
            assertEquals(counter.get(), 1);
        } finally {
            Plant.close();
        }
    }
}
