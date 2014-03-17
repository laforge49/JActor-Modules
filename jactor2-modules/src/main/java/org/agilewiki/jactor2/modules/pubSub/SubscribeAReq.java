package org.agilewiki.jactor2.modules.pubSub;

import org.agilewiki.jactor2.core.reactors.CommonReactor;
import org.agilewiki.jactor2.core.requests.AsyncRequest;
import org.agilewiki.jactor2.core.requests.AsyncResponseProcessor;

/**
 * A request to subscribe to the content published by a RequestBus.
 * Note that one of the processContent methods must be overridden or
 * an UnsupportedOperationException will be thrown when content is received.
 *
 * @param <CONTENT> The type of content.
 */
public class SubscribeAReq<CONTENT> extends AsyncRequest<Subscription<CONTENT>> {
    private final RequestBus<CONTENT> requestBus;
    private final CommonReactor subscriberReactor;
    private final Filter<CONTENT> filter;
    AsyncResponseProcessor<Subscription<CONTENT>> dis = this;

    /**
     * Creates a request to subscribe to all the content published by a RequestBus.
     * When invoked, the request passes back a subscription which can be used to unsubscribe.
     *
     * @param _requestBus        The RequestBus being subscribed to.
     * @param _subscriberReactor The reactor of the subscriber blade.
     */
    public SubscribeAReq(final RequestBus<CONTENT> _requestBus,
                         final CommonReactor _subscriberReactor) {
        this(_requestBus, _subscriberReactor, null);
    }

    /**
     * Creates a request to subscribe to selected content published by a RequestBus.
     *
     * @param _requestBus        The RequestBus being subscribed to.
     * @param _subscriberReactor The reactor of the subscriber blade.
     * @param _filter            A Filter that selects content of interest.
     */
    public SubscribeAReq(final RequestBus<CONTENT> _requestBus,
                         final CommonReactor _subscriberReactor,
                         final Filter<CONTENT> _filter) {
        super(_subscriberReactor);
        requestBus = _requestBus;
        subscriberReactor = _subscriberReactor;
        filter = _filter;
    }

    @Override
    public void processAsyncRequest() throws Exception {
        final Subscription<CONTENT> subscription = new Subscription<CONTENT>(
                requestBus, (CommonReactor) subscriberReactor, filter) {
            @Override
            protected void processContent(final CONTENT _content,
                                          final AsyncRequest<Void> _asyncRequest)
                    throws Exception {
                SubscribeAReq.this.processContent(_content,
                        _asyncRequest);
            }
        };
        requestBus.subscriptions.add(subscription);
        subscriberReactor.addCloseable(subscription);
        dis.processAsyncResponse(subscription);
    }

    /**
     * Process the content of interest using the reactor of the subscriber.
     *
     * @param _content The received content.
     */
    protected void processContent(final CONTENT _content) throws Exception {
        throw new UnsupportedOperationException(
                "The processContent method was not overridden.");
    }

    /**
     * Process the content of interest using the reactor of the subscriber.
     *
     * @param _content                The received content.
     * @param _asyncRequest Used to indicate when processing is complete.
     */
    protected void processContent(final CONTENT _content,
                                  final AsyncRequest<Void> _asyncRequest)
            throws Exception {
        processContent(_content);
        _asyncRequest.processAsyncResponse(null);
    }
}
